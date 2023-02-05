package com.cmc12th.runway.ui.photoreview

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROUTE
import kotlin.math.roundToInt

@Composable
fun PhotoReviewScreen(appState: ApplicationState) {
    val selectImages = remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            selectImages.value = url
        }

    val event = remember {
        mutableStateOf(false)
    }
    val croppedImage = remember {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(key1 = Unit) {
        galleryLauncher.launch("image/*")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackIcon()
            Text(text = "포토 후기 작성하기")
            Button(onClick = { event.value = !event.value }) {
                Text(text = "리뷰 작성완료")
            }
        }

        selectImages.value?.let {
            ModifyImage(it, event.value) {
                appState.navController.currentBackStackEntry?.arguments?.putParcelable(
                    "bitmap",
                    it
                )
                appState.navController.navigate(PHOTO_REVIEW_RESULT_ROUTE)
                croppedImage.value = it
            }
        }

    }
}

fun captureView(view: View, window: Window, bitmapCallback: (Bitmap) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Above Android O, use PixelCopy
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        PixelCopy.request(
            window,
            Rect(
                location[0], location[1],
                (location[0] + view.width), (location[1] + view.height)
            ),
            bitmap,
            { it ->
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        val tBitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.RGB_565
        )
        val canvas = Canvas(tBitmap)
        view.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}

@Composable
private fun ColumnScope.ModifyImage(
    selectImages: Uri,
    event: Boolean,
    generateBitmap: (Bitmap) -> Unit
) {
    val view = LocalView.current
    val activity = LocalContext.current as Activity

    LaunchedEffect(key1 = event) {
        if (event) {
            captureView(view, activity.window) {
                generateBitmap(it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberImagePainter(selectImages),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        TestTextField()
        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_nav_home_on),
                contentDescription = "IC_ADD"
            )
        }
    }
}


@Composable
private fun TestTextField() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val textField = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val fontSize = remember {
        mutableStateOf(24)
    }
    val fontColor = remember {
        mutableStateOf(Color.Black)
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
    ) {

        Column() {
            Row(modifier = Modifier.align(Alignment.End)) {
                Text(text = "검정색", modifier = Modifier.clickable {
                    fontColor.value = Color.Black
                })
                Text(text = "흰색", modifier = Modifier.clickable {
                    fontColor.value = Color.White
                })
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Font-UP",
                    modifier = Modifier.clickable {
                        fontSize.value++
                    }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_home_on),
                    contentDescription = "Font-Down",
                    modifier = Modifier.clickable {
                        fontSize.value--
                    }
                )
            }
            BasicTextField(
                value = textField.value,
                onValueChange = { textField.value = it },
                textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                    color = fontColor.value,
                    fontSize = fontSize.value.sp,
                ),
                modifier = Modifier.wrapContentSize(),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box() {
                            if (textField.value.text.isEmpty()) {
                                Text(
                                    "내용을 입력해주세요.",
                                    style = androidx.compose.material3.LocalTextStyle.current.copy(
                                        color = Color.Black.copy(alpha = 0.3f),
                                        fontSize = fontSize.value.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                },
            )
        }

    }
}
