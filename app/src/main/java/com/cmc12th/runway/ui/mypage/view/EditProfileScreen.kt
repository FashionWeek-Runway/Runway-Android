@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.mypage.view

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.broadcast.ComposeFileProvider
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.BottomSheetUsingItemLists
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.domain.model.signin.ProfileImageType
import com.cmc12th.runway.ui.signin.view.InputNickname
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.MYPAGE_EDIT_PROFILE_COMPLETE_ROUTE
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    appState: ApplicationState,
    viewModel: MypageViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomsheetState = rememberBottomSheet()
    val uiState by viewModel.profileImageUiState.collectAsStateWithLifecycle()
    val isKeyboardOpen by keyboardAsState() // Keyboard.Opened or Keyboard.Closed

    LaunchedEffect(Unit) {
        viewModel.getMyProfile()
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
            viewModel.updateProfileImage(image)
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
            viewModel.modifyProfile(
                onSuccess = {
                    appState.navController.navigate(MYPAGE_EDIT_PROFILE_COMPLETE_ROUTE)
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

    BottomSheetUsingItemLists(
        bottomsheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackIcon {
                    appState.navController.popBackStack()
                }
                Text(text = "프로필 편집", style = Body1B, color = Black)
            }

            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .weight(1f)
            ) {
                HeightSpacer(height = 30.dp)
                /** 프로필이미지 아이콘 */
                ProfileImageIcon(
                    profileImageType = uiState.profileImage,
                    profileSize = profileSize,
                    galleryLauncher = galleryLauncher,
                    takePhotoFromCameraLauncher = takePhotoFromCameraLauncher,
                    updateImageUri = { imageUri = it },
                    showBottomSheet = showBottomSheet,
                    setDefaultProfileImage = {
                        viewModel.updateProfileImage(
                            ProfileImageType.DEFAULT
                        )
                    }
                )

                /** 닉네임 입력 칸 */
                HeightSpacer(height = heightSpacerSize)
                InputNickname(
                    nickname = uiState.nickName,
                    errorMessage = errorMessage.value,
                    updateNickName = {
                        errorMessage.value = ""
                        viewModel.updateNickName(it)
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
                    text = "저장",
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
                        galleryLauncher = galleryLauncher,
                        showBottomSheet = showBottomSheet,
                        updateImageUri = updateImageUri,
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
                modifier = Modifier
                    .background(Gray200)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImage.imgUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_defailt_profile),
                contentDescription = "IMG_SELECTED_IMG",
                contentScale = ContentScale.Crop,
            )
        }
        if (selectedImage is ProfileImageType.LOCAL) {
            AsyncImage(
                modifier = Modifier
                    .background(Gray200)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImage.uri)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_defailt_profile),
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
    showBottomSheet: (BottomSheetContent) -> Unit,
    updateImageUri: (Uri?) -> Unit,
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