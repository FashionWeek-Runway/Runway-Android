@file:OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)

package com.cmc12th.runway.ui.signin

import android.graphics.Rect
import android.net.Uri
import android.view.ViewTreeObserver
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.*

@Composable
fun SignInProfileImage(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val onkeyboardFocus = remember {
        mutableStateOf(false)
    }
    val isKeyboardOpen by keyboardAsState() // Keyboard.Opened or Keyboard.Closed
    val profileSize by animateFloatAsState(
        targetValue = if (isKeyboardOpen == Keyboard.Opened) 0.4f else 0.66f, animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val heightSpacerSize by animateDpAsState(
        targetValue = if (isKeyboardOpen == Keyboard.Opened) 20.dp else 50.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val selectImage = remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            selectImage.value = url
        }



    Column(
        modifier = Modifier
            .imePadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(5)

        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            HeightSpacer(height = 20.dp)
            Row {
                Text(text = "프로필", style = HeadLine3)
                Text(text = "을 설정해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
            HeightSpacer(height = 40.dp)

            /** 프로필이미지 아이콘 */
            ProfileImageIcon(selectImage.value, profileSize, galleryLauncher)

            /** 닉네임 입력 칸 */
            HeightSpacer(height = heightSpacerSize)
            InputNickname() {
                onkeyboardFocus.value = it
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Gray300)
        ) {
            Text(
                text = "다음",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp
            )
        }

    }


}


@Composable
fun InputNickname(onFocuseChanged: (Boolean) -> Unit) {

    val nickNameTextField = remember {
        mutableStateOf(TextFieldValue(""))
    }
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                onFocuseChanged(it.hasFocus)
            },
        fontSize = 16.sp,
        value = nickNameTextField.value,
        placeholderText = "닉네임 입력",
        onvalueChanged = { if (it.text.length <= 10) nickNameTextField.value = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
        }),
    )
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
fun ProfileImageIcon(
    selectedImage: Uri?,
    profileSize: Float,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(profileSize)
                .aspectRatio(1f)
                .background(Color.Transparent, shape = CircleShape)
                .border(BorderStroke(1.dp, Gray300), shape = CircleShape),
            shape = CircleShape
        ) {
            if (selectedImage == null) {
                DefaultProfileImage(galleryLauncher)
            } else {
                SelectedProfileImage(selectedImage, galleryLauncher)
            }
        }

    }

}

@Composable
private fun SelectedProfileImage(
    selectedImage: Uri,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(model = selectedImage),
            contentScale = ContentScale.Crop,
            contentDescription = "IMG_DUMMY"
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.24f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        galleryLauncher.launch("image/*")
                    }
            ) {
                drawRect(color = Color(0x600A0A0A))
            }
            Text(
                text = "편집", style = Body1M, color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DefaultProfileImage(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.85f)
                .background(Gray50),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxHeight(0.70f)
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.ic_nav_mypage_on),
                contentDescription = "IC_NAV_MYPAGE_ON",
                tint = Gray200
            )
            Box {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                ) {
                    drawRect(color = Gray600)
                }
                Text(
                    text = "편집", style = Body1M, color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}
