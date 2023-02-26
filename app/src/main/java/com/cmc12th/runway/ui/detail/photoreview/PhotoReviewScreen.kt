@file:OptIn(
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.detail.photoreview

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.detail.photoreview.EditUiStatus.Companion.REVIEW_FONT_COLORS
import com.cmc12th.runway.ui.detail.photoreview.UserReviewText.Companion.DEFAULT_REVIEW_FONT_SIZE
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROUTE
import com.cmc12th.runway.utils.captureView
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class UserReviewText(
    val idx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textField: TextFieldValue = TextFieldValue(""),
    val textAlign: TextAlign = TextAlign.Start,
    val focusRequester: FocusRequester = FocusRequester(),
) {
    fun toEditUiStatus() = EditUiStatus(
        isEdit = true,
        editIdx = idx,
        fontSize = fontSize,
        textAlign = textAlign,
        fontColor = fontColor,
        textField = textField,
    )

    companion object {
        val DEFAULT_REVIEW_FONT_SIZE = 31.sp
    }
}

data class EditUiStatus(
    val isEdit: Boolean,
    val editIdx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textAlign: TextAlign = TextAlign.Start,
    val isColorPickerVisibility: Boolean = false,
    val textField: TextFieldValue = TextFieldValue(""),
) {
    companion object {

        val REVIEW_FONT_COLORS = listOf(
            Color.White,
            Color.Black,
            Primary,
            Point,
            Color(0xFFFBFF28),
            Color(0xFFFC3A56),
            Color(0xFFD700E7)
        )

        fun disabled() =
            EditUiStatus(
                false,
                -1,
                DEFAULT_REVIEW_FONT_SIZE,
                Color.White,
                TextAlign.Start,
                false,
                TextFieldValue("")
            )
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
    val configuration = LocalConfiguration.current
    val textFieldWidth = (configuration.screenWidthDp.absoluteValue - 60.dp.value).dp

    val focusManager = LocalFocusManager.current
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
                    },
                    textFieldWidth = textFieldWidth
                )
            }

            if (editStatus.value.isEdit) {
                /** 검정색 백그라운드 */
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x50000000))
                    .clickable {
                        addUserReviewText()
                        keyboardController?.hide()
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
            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier
                    .imePadding()
                    .align(Alignment.BottomCenter)
                    .padding(
                        0.dp,
                        20.dp
                    ),
                enter = fadeIn(),
                exit = fadeOut(),
                visible = editStatus.value.isEdit && editStatus.value.isColorPickerVisibility
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        WidthSpacer(width = 0.dp)
                    }
                    items(REVIEW_FONT_COLORS) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(it)
                                .border(
                                    BorderStroke(
                                        if (editStatus.value.fontColor == it) 3.dp else 1.dp,
                                        Color.White
                                    ), CircleShape
                                )
                                .clickable {
                                    editStatus.value = editStatus.value.copy(
                                        fontColor = it
                                    )
                                }
                        )
                    }
                    item {
                        WidthSpacer(width = 0.dp)
                    }
                }
            }


        }

        AnimatedVisibility(visible = !editStatus.value.isEdit) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { event.value = !event.value },
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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_right_arrow_16),
                            contentDescription = "IC_RIGHT_ARROW",
                            modifier = Modifier.size(16.dp),
                            tint = Point
                        )
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
    textFieldWidth: Dp,
) {
    val focusRequester = remember {
        FocusRequester()
    }

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
            color = editUiState.fontColor,
            fontSize = editUiState.fontSize,
            lineHeight = editUiState.fontSize * 1.4f,
            fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_medium)),
            textAlign = editUiState.textAlign
        ),
        modifier = Modifier
            .offset(x = 60.dp, y = 160.dp)
            .width(textFieldWidth)
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
    )
}

@Composable
private fun BoxScope.FontSizeToolBar(
    editUiState: EditUiStatus,
    updateEditUiState: (EditUiStatus) -> Unit,
) {
    var offsetY by remember { mutableStateOf(0.dp.value) }
    LaunchedEffect(key1 = Unit) {
        offsetY = (editUiState.fontSize.value - 31.sp.value) * 19f * -1
    }
    Box(
        modifier = Modifier.Companion
            .align(Alignment.TopStart)
            .offset(x = 20.dp, y = 100.dp)
            .height(240.dp)
            .width(24.dp)
    ) {
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
                        val my = if (dy >= 0.dp.value) kotlin.math.min(
                            300.dp.value,
                            dy
                        ) else kotlin.math.max(-300.dp.value, dy)

                        updateEditUiState(
                            editUiState.copy(
                                fontSize = (31.sp.value + (my / 19f * -1).sp.value).sp,
                                textField = editUiState.textField
                            )
                        )

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
private fun BoxScope.TopBarIcons(
    isEdit: Boolean,
    updateEditMode: (isEdit: Boolean) -> Unit,
    addUserReviewText: () -> Unit,
    updateColorPickerVisiblity: (Boolean) -> Unit,
    editStatus: EditUiStatus,
    updateTextAlign: (TextAlign) -> Unit,
) {

    /** 에디트 모드일 때 */
    AnimatedVisibility(visible = isEdit, enter = fadeIn(), exit = fadeOut()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(78.dp)
                .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "취소", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                updateEditMode(false)
            })
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {

                when (editStatus.textAlign) {
                    TextAlign.Start -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_left_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Center)
                                },
                            tint = Color.White
                        )
                    }
                    TextAlign.Center -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_center_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Right)
                                },
                            tint = Color.White
                        )
                    }
                    else -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_right_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Start)
                                },
                            tint = Color.White
                        )
                    }
                }

                if (!editStatus.isColorPickerVisibility) {
                    Image(
                        painter = painterResource(id = R.drawable.img_color_picker_able_24),
                        contentDescription = "IMG_COLOR_PICKER_ABLE",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                updateColorPickerVisiblity(true)
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_color_picker_disable_24),
                        contentDescription = "IMG_COLOR_PICKER_DISABLE",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                updateColorPickerVisiblity(false)
                            }
                    )
                }

            }
            Text(text = "완료", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                addUserReviewText()
            })
        }
    }

    /** 에디트 모드가 아닐 때 */
    if (!isEdit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                updateEditMode(true)
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
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
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
