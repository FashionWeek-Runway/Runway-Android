@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.detail.photoreview

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.detail.DetailViewModel
import com.cmc12th.runway.ui.detail.photoreview.components.*
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus
import com.cmc12th.runway.ui.detail.photoreview.model.UserReviewText
import com.cmc12th.runway.ui.detail.photoreview.model.UserReviewText.Companion.DEFAULT_REVIEW_FONT_SIZE
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.utils.captureView
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@Composable
fun PhotoReviewScreen(appState: ApplicationState, idx: Int, uri: Uri?) {
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

    val ableEditStatus: (userReviewText: UserReviewText) -> Unit = {
        editStatus.value = it.toEditUiStatus()
    }

    LaunchedEffect(key1 = keyboardStatusState.value) {
        if (keyboardStatusState.value == KeyboardStatus.Closed) {
            disableEditStatus()
        }
    }

    LaunchedEffect(key1 = Unit) {
        appState.bottomBarState.value = false

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
                FontSizeToolBar(
                    updateEditUiState = { editStatus.value = it },
                    editUiState = editStatus.value,
                )
                EditFocusTextField(
                    editUiState = editStatus.value,
                    updateEditUiState = { editStatus.value = it },
                    textFieldWidth = textFieldWidth
                )
            }

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

        ComfirmButton(
            editStatus = editStatus.value,
            event = uploadEvent.value,
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
