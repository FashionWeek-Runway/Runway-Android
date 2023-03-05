@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.signin.view

import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.broadcast.ComposeFileProvider
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomBottomSheet
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.MAX_NICKNAME_LENGTH
import com.cmc12th.runway.utils.Constants.SIGNIN_CATEGORY_ROUTE
import kotlinx.coroutines.launch


@Composable
fun SignInProfileImageScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
    profileImage: String,
    kakaoId: String,
) {

    val uiState by signInViewModel.profileImageUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bottomsheetState = rememberBottomSheet()
    val isKeyboardOpen by keyboardAsState() // Keyboard.Opened or Keyboard.Closed

    val initFlag = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (initFlag.value) {
            initFlag.value = false
            if (profileImage.isNotBlank() && kakaoId.isNotBlank()) {
                signInViewModel.updateSignIntypeSocial(profileImage, kakaoId)
            }
        }
    }
    LaunchedEffect(key1 = isKeyboardOpen) {
        if (isKeyboardOpen == KeyboardStatus.Closed) {
            coroutineScope.launch {
                bottomsheetState.modalSheetState.hide()
            }
        }
    }

    val errorMessage = remember {
        mutableStateOf("")
    }

    val profileSize by animateFloatAsState(
        targetValue = if (isKeyboardOpen == KeyboardStatus.Opened) 0.35f else 0.66f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val heightSpacerSize by animateDpAsState(
        targetValue = if (isKeyboardOpen == KeyboardStatus.Opened) 10.dp else 30.dp,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val setImageToViewModel: (Uri?) -> Unit = { uri ->
        if (uri != null) {
            val image = ProfileImageType.LOCAL(uri = uri)
            signInViewModel.updateProfileImage(image)
        }
    }

    LaunchedEffect(key1 = hasImage) {
        if (hasImage) {
            setImageToViewModel(imageUri)
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            setImageToViewModel(uri)
        }

    val takePhotoFromCameraLauncher = // 카메라로 사진 찍어서 가져오기
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            hasImage = it
        }

    val onDone = {
        errorMessage.value = ""
        if (uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate()) {
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
    }

    val showBottomSheet: (BottomSheetContent) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.show()
        }
    }

    CustomBottomSheet(
        bottomsheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                BackIcon {
                    appState.navController.popBackStack()
                }
            }
            OnBoardStep(5)

            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .weight(1f)
            ) {
                HeightSpacer(height = 20.dp)
                Row {
                    Text(text = "프로필", style = HeadLine3)
                    Text(text = "을 설정해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
                }

                HeightSpacer(height = 30.dp)
                /** 프로필이미지 아이콘 */
                ProfileImageIcon(
                    profileImageType = uiState.profileImage,
                    profileSize = profileSize,
                    galleryLauncher = galleryLauncher,
                    takePhotoFromCameraLauncher = takePhotoFromCameraLauncher,
                    updateImageUri = { imageUri = it },
                    showBottomSheet = showBottomSheet
                ) { signInViewModel.updateProfileImage(ProfileImageType.DEFAULT) }

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
                    .padding(20.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate(),
                colors = ButtonDefaults.buttonColors(
                    if (uiState.nickName.text.isNotBlank() && uiState.nickName.checkValidate()) Black else Gray300
                )
            ) {
                Text(
                    text = "다음",
                    modifier = Modifier.padding(0.dp, 5.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
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
    takePhotoFromCameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    showBottomSheet: (BottomSheetContent) -> Unit,
    updateImageUri: (Uri?) -> Unit,
    setDefaultProfileImage: () -> Unit,
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
                is ProfileImageType.DEFAULT -> {
                    DefaultProfileImage(
                        galleryLauncher = galleryLauncher,
                        takePhotoFromCameraLauncher = takePhotoFromCameraLauncher,
                        updateImageUri = updateImageUri,
                        showBottomSheet = showBottomSheet
                    )
                }
                else -> {
                    SelectedProfileImage(
                        selectedImage = profileImageType,
                        takePhotoFromCameraLauncher = takePhotoFromCameraLauncher,
                        updateImageUri = updateImageUri,
                        galleryLauncher = galleryLauncher,
                        showBottomSheet = showBottomSheet,
                        setDefaultProfileImage = setDefaultProfileImage
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedProfileImage(
    selectedImage: ProfileImageType,
    takePhotoFromCameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    showBottomSheet: (BottomSheetContent) -> Unit,
    setDefaultProfileImage: () -> Unit,
    updateImageUri: (Uri?) -> Unit,
) {
    val context = LocalContext.current
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
                contentDescription = "IMG_SELECTED_IMG",
                contentScale = ContentScale.Crop,
            )
        }
        if (selectedImage is ProfileImageType.LOCAL) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImage.uri)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "IMG_SELECTED_IMG",
                contentScale = ContentScale.Crop,
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
                        showBottomSheet(
                            BottomSheetContent(
                                title = "", itemList = listOf(
                                    BottomSheetContentItem(
                                        itemName = "기본 이미지로 변경",
                                        onItemClick = { setDefaultProfileImage() },
                                        isSeleceted = false
                                    ),
                                    BottomSheetContentItem(
                                        itemName = "사진 촬영",
                                        onItemClick = {
                                            val uri = ComposeFileProvider.getImageUri(context)
                                            updateImageUri(uri)
                                            takePhotoFromCameraLauncher.launch(uri)
                                        },
                                        isSeleceted = false
                                    ),
                                    BottomSheetContentItem(
                                        itemName = "사진 가져오기",
                                        onItemClick = { galleryLauncher.launch("image/*") },
                                        isSeleceted = false
                                    )
                                )
                            )
                        )
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
private fun DefaultProfileImage(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    takePhotoFromCameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    updateImageUri: (Uri?) -> Unit,
    showBottomSheet: (BottomSheetContent) -> Unit,
) {
    val context = LocalContext.current

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
                            showBottomSheet(
                                BottomSheetContent(
                                    title = "", itemList = listOf(
                                        BottomSheetContentItem(
                                            itemName = "사진 촬영",
                                            onItemClick = {
                                                val uri = ComposeFileProvider.getImageUri(context)
                                                updateImageUri(uri)
                                                takePhotoFromCameraLauncher.launch(uri)
                                            },
                                            isSeleceted = false
                                        ),
                                        BottomSheetContentItem(
                                            itemName = "사진 가져오기",
                                            onItemClick = { galleryLauncher.launch("image/*") },
                                            isSeleceted = false
                                        )
                                    )
                                )
                            )
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
