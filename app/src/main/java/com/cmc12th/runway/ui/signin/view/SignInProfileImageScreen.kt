package com.cmc12th.runway.ui.signin.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.MAX_NICKNAME_LENGTH
import com.cmc12th.runway.utils.Constants.SIGNIN_CATEGORY_ROUTE

@Composable
fun SignInProfileImageScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
    profileImage: String,
    kakaoId: String,
) {

    val uiState by signInViewModel.profileImageUiState.collectAsStateWithLifecycle()

    val someFlag = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (someFlag.value) {
            someFlag.value = false
            if (profileImage.isNotBlank() && kakaoId.isNotBlank()) {
                signInViewModel.updateSignIntypeSocial(profileImage, kakaoId)
            }
        }
    }
    val errorMessage = remember {
        mutableStateOf("")
    }

    val isKeyboardOpen by keyboardAsState() // Keyboard.Opened or Keyboard.Closed
    val profileSize by animateFloatAsState(
        targetValue = if (isKeyboardOpen == KeyboardStatus.Opened) 0.4f else 0.66f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val heightSpacerSize by animateDpAsState(
        targetValue = if (isKeyboardOpen == KeyboardStatus.Opened) 20.dp else 50.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { url ->
            if (url != null) {
                val image = ProfileImageType.LOCAL(uri = url)
                signInViewModel.updateProfileImage(image)
            }
        }
    val onDone = {
        errorMessage.value = ""
        signInViewModel.checkNickname(
            onSuccess = {
                appState.navController.navigate(SIGNIN_CATEGORY_ROUTE)
            },
            onError = {
                errorMessage.value = it.message
                appState.showSnackbar(it.message)
            }
        )
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
            ProfileImageIcon(uiState.profileImage, profileSize, galleryLauncher)

            /** 닉네임 입력 칸 */
            HeightSpacer(height = heightSpacerSize)
            InputNickname(
                nickname = uiState.nickName,
                errorMessage = errorMessage.value,
                updateNickName = {
                    errorMessage.value = ""
                    signInViewModel.updateNickName(it)
                },
                onDone = {
                    if (uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate()) {
                        onDone()
                    }
                }
            )

        }

        /** 다음 버튼 */
        Button(
            onClick = {
                onDone()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape,
            enabled = uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate(),
            colors = ButtonDefaults.buttonColors(
                if (uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate()) Black else Gray300
            )
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
fun InputNickname(
    nickname: Nickname,
    updateNickName: (String) -> Unit,
    errorMessage: String,
    onDone: () -> Unit,
) {
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 16.sp,
        value = nickname.text,
        placeholderText = "닉네임 입력",
        onvalueChanged = {
            if (it.length <= MAX_NICKNAME_LENGTH) updateNickName(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        }),
        onErrorState = errorMessage.isNotBlank() || (nickname.text.isNotBlank() && !nickname.checkValidate()),
        errorMessage = errorMessage.ifBlank { "닉네임은 한글, 영어 혼합 2~10글자 입니다." }
    )
}

@Composable
fun ProfileImageIcon(
    profileImageType: ProfileImageType,
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
            when (profileImageType) {
                is ProfileImageType.DEFAULT -> DefaultProfileImage(galleryLauncher)
                else -> {
                    SelectedProfileImage(
                        profileImageType,
                        galleryLauncher
                    )
                }

            }
        }
    }
}

@Composable
private fun SelectedProfileImage(
    selectedImage: ProfileImageType,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
) {
    Box {
        if (selectedImage is ProfileImageType.SOCIAL) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImage.imgUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "ASDas",
                contentScale = ContentScale.Crop,
            )
        }
        if (selectedImage is ProfileImageType.LOCAL) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = selectedImage.uri
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "IMG_DUMMY"
            )
        }
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
