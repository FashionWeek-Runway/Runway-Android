@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.detail.photoreview.view

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.detail.DetailViewModel
import com.cmc12th.runway.ui.detail.components.*
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus
import com.cmc12th.runway.ui.detail.photoreview.model.UserReviewText
import com.cmc12th.runway.ui.detail.photoreview.model.UserReviewText.Companion.DEFAULT_REVIEW_FONT_SIZE
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.utils.toBitmap
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@Composable
fun ReviewWriteScreen(appState: ApplicationState, idx: Int, uri: Uri?) {
    val selectImages = remember { mutableStateOf(uri) }
    val detailViewModel: DetailViewModel = hiltViewModel()

    val uploadEvent = remember {
        mutableStateOf(false)
    }
    val editStatus = remember {
        mutableStateOf(EditUiStatus.disabled())
    }
    val userReviewText = remember {
        mutableStateListOf<UserReviewText>()
    }
    val keyboardStatusState = keyboardAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val textFieldWidth = (configuration.screenWidthDp.absoluteValue - 60.dp.value).dp

    val disableEditStatus = {
        focusManager.clearFocus()
        editStatus.value = EditUiStatus.disabled()
    }

    val addUserReviewText = {
        if (editStatus.value.editIdx != -1) {
            userReviewText[editStatus.value.editIdx] =
                userReviewText[editStatus.value.editIdx].copy(
                    fontSize = editStatus.value.fontSize,
                    fontColor = editStatus.value.fontColor,
                    textField = editStatus.value.textField,
                    textAlign = editStatus.value.textAlign
                )
        }
        disableEditStatus()
    }

    val bottomBarHeight = remember {
        mutableStateOf(0.dp)
    }

    val ableEditStatus: (userReviewText: UserReviewText) -> Unit = {
        editStatus.value = it.toEditUiStatus()
    }

    LaunchedEffect(key1 = keyboardStatusState.value) {
        if (keyboardStatusState.value == KeyboardStatus.Closed) {
            disableEditStatus()
        }
    }

    LaunchedEffect(key1 = Unit) {
    }
    appState.systmeUiController.setSystemBarsColor(Color.Black)
    appState.systmeUiController.setNavigationBarColor(Color.Black)

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

            /** 비트맵 이미지를 생성하는 뷰  */
            selectImages.value?.let {
                GenerateImageView(
                    selectImages = it,
                    event = uploadEvent.value,
                    updateEvent = { uploadEvent.value = it },
                    generateBitmap = { bitmap ->
                        detailViewModel.addUserReview(idx, bitmap) {
                            appState.popBackStack()
                        }
                    },
                    userReviewText = userReviewText,
                    updateUserReviewText = { idx, userReview ->
                        userReviewText[idx] = userReview
                    },
                    editIdx = editStatus.value.editIdx,
                    updateEditMode = { editMode, userReview ->
                        if (editMode) ableEditStatus(userReview) else disableEditStatus()
                    },
                    bottomBarHeight = bottomBarHeight.value,
                    textFieldWidth = textFieldWidth
                )
            }

            if (editStatus.value.isEdit) {
                /** 검정색 백그라운드 */
                EditBackGround(onClick = {
                    keyboardController?.hide()
                    addUserReviewText()
                })
                /** 폰트 사이즈 조절 */
                var offsetX by remember { mutableStateOf(0.dp) }
                val animatedOffsetX by animateDpAsState(targetValue = offsetX)

                LaunchedEffect(key1 = editStatus.value.textField) {
                    if (editStatus.value.textField.text.isNotBlank()) {
                        offsetX = (-30).dp
                    }
                }

                Box(modifier = Modifier.offset(x = animatedOffsetX)) {
                    FontSizeToolBar(
                        updateEditUiState = { editStatus.value = it },
                        editUiState = editStatus.value,
                        updateOffsetX = { offsetX = it }
                    )
                    EditFocusTextField(
                        editUiState = editStatus.value,
                        updateEditUiState = { editStatus.value = it },
                        textFieldWidth = textFieldWidth
                    )
                }
            }

            // 이미지 업로드할 때(스샷 찍을때는 숨겨야함)
            if (!uploadEvent.value) {
                /** 탑 바 아이콘 모음 */
                TopBarIcons(
                    popBackStack = { appState.popBackStack() },
                    isEdit = editStatus.value.isEdit,
                    editStatus = editStatus.value,
                    updateColorPickerVisiblity = {
                        editStatus.value = editStatus.value.copy(
                            isColorPickerVisibility = it
                        )
                    },
                    updateTextAlign = {
                        editStatus.value = editStatus.value.copy(
                            textAlign = it
                        )
                    },
                    updateEditMode = { editMode ->
                        if (editMode) {
                            val userReview = UserReviewText(
                                idx = userReviewText.size,
                                fontSize = DEFAULT_REVIEW_FONT_SIZE,
                                fontColor = Color.White,
                            )
                            ableEditStatus(userReview)
                            userReviewText.add(userReview)
                        } else {
                            disableEditStatus()
                        }
                    },
                    addUserReviewText = addUserReviewText
                )

                /** 색상 선택 리스트 */
                ReviewTextColorPicker(
                    editStatus = editStatus.value,
                    updateEditUiStatus = { editStatus.value = it }
                )

            }
        }

        ComfirmButton(
            editStatus = editStatus.value,
            event = uploadEvent.value,
            updateBottomBarHeight = { bottomBarHeight.value = it },
            updateUploadEvent = {
                uploadEvent.value = it
            }
        )

    }
}

@Composable
private fun EditBackGround(
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0x50000000))
        .clickable {
            onClick()
        })
}

@Composable
private fun GenerateImageView(
    selectImages: Uri,
    event: Boolean,
    generateBitmap: (Bitmap) -> Unit,
    userReviewText: SnapshotStateList<UserReviewText>,
    updateUserReviewText: (idx: Int, userReviewText: UserReviewText) -> Unit,
    updateEditMode: (isEdit: Boolean, userReviewText: UserReviewText) -> Unit,
    editIdx: Int,
    textFieldWidth: Dp,
    bottomBarHeight: Dp,
    updateEvent: (Boolean) -> Unit,
) {
    val view = LocalView.current
    val localDensity = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarsHeight =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(key1 = event) {
        if (event) {
            delay(100L)
            view.toBitmap(
                topbarheight = with(localDensity) { statusBarHeight.toPx() },
                bottomBarHeight = with(localDensity) { navigationBarsHeight.toPx() + bottomBarHeight.toPx() },
                onBitmapReady = { bitmap: Bitmap ->
                    generateBitmap(bitmap)
                },
                onBitmapError = { _: Exception ->
                    // TODO - handle exception
                }
            )
        }
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = rememberAsyncImagePainter(selectImages),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
    userReviewText.forEach {
        ReviewTextField(
            editIdx = editIdx,
            updateEditMode = updateEditMode,
            userReviewText = it,
            updateUserReviewText = updateUserReviewText,
            textFieldWidth = textFieldWidth,
        )
    }
}


@Composable
private fun ReviewTextField(
    userReviewText: UserReviewText,
    updateUserReviewText: (idx: Int, userReviewText: UserReviewText) -> Unit,
    updateEditMode: (isEdit: Boolean, userReviewText: UserReviewText) -> Unit,
    editIdx: Int,
    textFieldWidth: Dp,
) {
    val configuration = LocalConfiguration.current
    var offsetX by remember { mutableStateOf(configuration.screenWidthDp.dp.value / 2) }
    var offsetY by remember { mutableStateOf(configuration.screenHeightDp.dp.value / 2) }

    if (editIdx != userReviewText.idx) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        offsetX.roundToInt(),
                        offsetY.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                },
        ) {
            BasicTextField(
                value = userReviewText.textField,
                readOnly = true,
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
                    lineHeight = userReviewText.fontSize * 1.4f,
                    fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_medium)),
                    textAlign = userReviewText.textAlign
                ),
                modifier = Modifier
                    .width(textFieldWidth)
                    .wrapContentHeight()
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

                }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
        }
    }
}
