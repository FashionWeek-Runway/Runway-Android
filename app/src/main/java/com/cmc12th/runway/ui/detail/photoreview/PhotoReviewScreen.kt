@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.detail.photoreview

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROUTE
import com.cmc12th.runway.utils.captureView
import kotlin.math.roundToInt

data class UserReviewText(
    val idx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textField: String = "",
    val focusRequester: FocusRequester = FocusRequester(),
) {
    fun toEditUiStatus() = EditUiStatus(
        isEdit = true,
        editIdx = idx,
        fontSize = fontSize,
        fontColor = fontColor,
        textField = textField,
    )

    companion object {
        fun disabled() = UserReviewText(-1, 14.sp, Color.White, "")
    }
}

data class EditUiStatus(
    val isEdit: Boolean,
    val editIdx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textField: String = "",
) {
    companion object {
        fun disabled() = EditUiStatus(false, -1, 14.sp, Color.White, "")
    }
}

@Composable
fun PhotoReviewScreen(appState: ApplicationState) {
    val selectImages = remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            appState.systmeUiController.setSystemBarsColor(Color.Black)
            appState.systmeUiController.setNavigationBarColor(Color.Black)
            selectImages.value = url
        }

    val event = remember {
        mutableStateOf(false)
    }
    val croppedImage = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val editStatus = remember {
        mutableStateOf(EditUiStatus.disabled())
    }
    val userReviewText = remember {
        mutableStateListOf<UserReviewText>()
    }
    val keyboardStatusState = keyboardAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current
    val disableEditStatus = {
//        userReviewText.forEach {
//            it.focusRequester.freeFocus()
//        }
        focusManager.clearFocus()
        if (editStatus.value.editIdx != -1) {
            userReviewText[editStatus.value.editIdx] =
                userReviewText[editStatus.value.editIdx].copy(
                    fontSize = editStatus.value.fontSize,
                    fontColor = editStatus.value.fontColor,
                    textField = editStatus.value.textField
                )
        }
        editStatus.value = EditUiStatus.disabled()
    }

    val ableEditStatus: (userReviewText: UserReviewText) -> Unit = {
        editStatus.value = it.toEditUiStatus()
    }

    LaunchedEffect(key1 = keyboardStatusState.value) {
        if (keyboardStatusState.value == KeyboardStatus.Closed) {
            disableEditStatus()
        }
    }

    DisposableEffect(Unit) {
        galleryLauncher.launch("image/*")
        onDispose {
            appState.systmeUiController.setSystemBarsColor(Color.White)
            appState.systmeUiController.setNavigationBarColor(Color.White)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            /** 뽑아낼 비트맵 이미지 모임 */
            selectImages.value?.let {
                ModifyImage(
                    selectImages = it,
                    event = event.value,
                    generateBitmap = {
                        appState.navController.currentBackStackEntry?.arguments?.putParcelable(
                            "bitmap",
                            it
                        )
                        appState.navController.navigate(PHOTO_REVIEW_RESULT_ROUTE)
                        croppedImage.value = it
                    },
                    userReviewText = userReviewText,
                    updateUserReviewText = { idx, userReview ->
                        userReviewText[idx] = userReview
                    },
                    editIdx = editStatus.value.editIdx,
                    updateEditMode = { editMode, userReview ->
                        if (editMode) ableEditStatus(userReview) else disableEditStatus()
                    }
                )
            }

            if (editStatus.value.isEdit) {
                /** 검정색 백그라운드 */
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x50000000))
                    .clickable {
                        keyboardController?.hide()
                    })
                /** 폰트 사이즈 조절 */
                FontSizeToolBar(
                    updateEditUiState = { editStatus.value = it },
                    editUiState = editStatus.value,
                )
                EditFocusTextField(
                    editUiState = editStatus.value,
                    updateEditUiState = { editStatus.value = it }
                )
            }

            /** 탑 바 아이콘 모음 */
            TopBarIcons(
                isEdit = editStatus.value.isEdit,
                updateEditMode = { editMode, userReview ->
                    if (editMode) ableEditStatus(userReview) else disableEditStatus()
                },
                addUserReviewText = {
                    val userReview = UserReviewText(
                        idx = userReviewText.size,
                        fontSize = 14.sp,
                        fontColor = Color.White
                    )
                    ableEditStatus(userReview)
                    userReviewText.add(userReview)
                }
            )


        }

        AnimatedVisibility(visible = !editStatus.value.isEdit) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { event.value = !event.value },
                    modifier = Modifier
                        .padding(12.dp, 20.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Gray700),
                    colors = ButtonDefaults.buttonColors(Color(0x50242528))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "등록", style = Body2B, color = Point)
                        WidthSpacer(width = 4.dp)
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_right_arrow_16),
                            contentDescription = "IC_RIGHT_ARROW",
                            modifier = Modifier.size(16.dp),
                            tint = Point)
                    }
                }

            }
        }

    }
}

@Composable
private fun EditFocusTextField(
    updateEditUiState: (EditUiStatus) -> Unit,
    editUiState: EditUiStatus,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    BasicTextField(
        value = editUiState.textField,
        onValueChange = {
            updateEditUiState(
                editUiState.copy(
                    textField = it
                )
            )
        },
        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = editUiState.fontSize,
            lineHeight = editUiState.fontSize * 1.4f
        ),
        modifier = Modifier
            .offset(x = 60.dp, y = 250.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .focusRequester(focusRequester)
            .onFocusChanged {
            },
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                innerTextField()
            }
        },
        keyboardActions = KeyboardActions(onDone = {

        }))
}

@Composable
private fun BoxScope.FontSizeToolBar(
    editUiState: EditUiStatus,
    updateEditUiState: (EditUiStatus) -> Unit,
) {
    var offsetY by remember { mutableStateOf(0.dp.value) }

    Box(modifier = Modifier.Companion
        .align(Alignment.TopStart)
        .offset(x = 20.dp, y = 100.dp)
        .height(240.dp)
        .width(24.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(path = Path().apply {
                moveTo(size.width / 2f, size.height)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }, color = Color(0x50EEF0F3))
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(0, y = offsetY.roundToInt())
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val dy = offsetY + delta
                        val my = if (dy >= 0.dp.value) kotlin.math.min(250.dp.value,
                            dy) else kotlin.math.max(-250.dp.value, dy)

                        updateEditUiState(editUiState.copy(
                            fontSize = (31.sp.value + (my / 19f * -1).sp.value).sp,
                            textField = editUiState.textField
                        ))

                        offsetY = my
                    }
                ),
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                    ) { change, dragAmount ->
//                        change.consume()
//                        val dy = offsetY + dragAmount.y
//                        val my = if (dy >= 0.dp.value) kotlin.math.min(250.dp.value,
//                            dy) else kotlin.math.max(-250.dp.value, dy)
//                        updateEditUiState(editUiState.copy(
//                            fontSize = (((my + 250.dp.value) / 5.dp.value) * 38.sp.value + 12.sp.value).sp,
//                            textField = editUiState.textField
//                        ))
//                        offsetY = my
//                    }
//                }
        ) {
            drawCircle(
                color = Color.White,
                radius = size.width / 2,
            )
        }
    }
}

@Composable
private fun TopBarIcons(
    isEdit: Boolean,
    updateEditMode: (isEdit: Boolean, userReviewText: UserReviewText) -> Unit,
    addUserReviewText: () -> Unit,
) {
    /** 에디트 모드일 때 */
    AnimatedVisibility(visible = isEdit, enter = fadeIn(), exit = fadeOut()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "취소", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                updateEditMode(false, UserReviewText.disabled())
            })
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_left_align_24),
                    contentDescription = "IMG_COLOR_PICKER",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )

                Image(
                    painter = painterResource(id = R.drawable.img_color_picker_24),
                    contentDescription = "IMG_COLOR_PICKER",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(text = "완료", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                updateEditMode(false, UserReviewText.disabled())
            })
        }
    }

    /** 에디트 모드가 아닐 때 */
    if (!isEdit) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {

                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_runway),
                    contentDescription = "IC_ARROW",
                    tint = Color.White
                )
            }
            IconButton(onClick = {
                addUserReviewText()
            }) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(31.dp))
                        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(31.dp))
                        .padding(10.dp, 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "IC_PLUS",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(text = "Aa", style = Body2M, color = Color.White)
                }
            }
        }
    }
}


@Composable
private fun ModifyImage(
    selectImages: Uri,
    event: Boolean,
    generateBitmap: (Bitmap) -> Unit,
    userReviewText: SnapshotStateList<UserReviewText>,
    updateUserReviewText: (idx: Int, userReviewText: UserReviewText) -> Unit,
    updateEditMode: (isEdit: Boolean, userReviewText: UserReviewText) -> Unit,
    editIdx: Int,
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

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = rememberAsyncImagePainter(selectImages),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
    userReviewText.forEach {
        TestTextField(
            editIdx = editIdx,
            updateEditMode = updateEditMode,
            userReviewText = it,
            updateUserReviewText = updateUserReviewText,
        )
    }
}


@Composable
private fun TestTextField(
    userReviewText: UserReviewText,
    updateUserReviewText: (idx: Int, userReviewText: UserReviewText) -> Unit,
    updateEditMode: (isEdit: Boolean, userReviewText: UserReviewText) -> Unit,
    editIdx: Int,
) {
    val configuration = LocalConfiguration.current
    var offsetX by remember { mutableStateOf(configuration.screenWidthDp.dp.value / 2) }
    var offsetY by remember { mutableStateOf(configuration.screenHeightDp.dp.value / 2) }

//    val focusRequester = remember {
//        FocusRequester()
//    }

//    LaunchedEffect(key1 = Unit) {
//        userReviewText.focusRequester.requestFocus()
//    }

    if (editIdx != userReviewText.idx) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        offsetX.roundToInt(),
                        offsetY.roundToInt())
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }

                },
        ) {
            Column() {
                BasicTextField(
                    value = userReviewText.textField,
                    onValueChange = {
                        updateUserReviewText(
                            userReviewText.idx,
                            userReviewText.copy(
                                textField = it
                            )
                        )
                    },
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                        color = userReviewText.fontColor,
                        fontSize = userReviewText.fontSize,
                        lineHeight = userReviewText.fontSize * 1.4f
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .onFocusChanged {
                            if (it.hasFocus) {
                                updateEditMode(true, userReviewText)
                            }
                        }
                        .focusRequester(userReviewText.focusRequester),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            innerTextField()
                        }
                    },
                    keyboardActions = KeyboardActions(onDone = {

                    })
                )
            }

        }
    }
}
