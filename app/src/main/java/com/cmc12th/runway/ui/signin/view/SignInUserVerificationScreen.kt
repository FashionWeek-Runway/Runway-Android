@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class
)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.*
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.domain.model.signin.*
import com.cmc12th.domain.model.signin.Birth.Companion.BIRTH_LENGTH
import com.cmc12th.domain.model.signin.Phone.Companion.PHONE_NUMBER_LENGTH
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_PHONE_VERIFY_ROUTE
import com.cmc12th.runway.utils.lookupLogEvent
import kotlinx.coroutines.launch

@Composable
fun SignInUserInfoVerifyScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomsheetState = rememberBottomSheet()
    val keyboardController = LocalSoftwareKeyboardController.current
    val showBottomSheet: (BottomSheetContent) -> Unit = {
        keyboardController?.hide()
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.show()
        }
    }

    BottomSheetUsingItemLists(
        bottomsheetState
    ) {
        UserVerificationContents(
            keyboardController,
            appState,
            showBottomSheet,
            signInViewModel,
        )
    }
}

@Composable
private fun UserVerificationContents(
    keyboardController: SoftwareKeyboardController?,
    appState: ApplicationState,
    showBottomSheet: (BottomSheetContent) -> Unit,
    viewmodel: SignInViewModel,
) {
    val keyboardState by keyboardAsState()

    /** 핸드폰 TextField가 포커스일 때 로그인 버튼이 올라오게 하기위해 */
    val isPhoneFocused = remember {
        mutableStateOf(false)
    }
    val uiState by viewmodel.userVerificationUiState.collectAsStateWithLifecycle()
    val (birthFocusRequest, phoneFocusRequest) = remember { FocusRequester.createRefs() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val passwordErrorMessage = remember {
        mutableStateOf("")
    }

    val closeInitialDialog = { viewmodel.updateDialogState(false) }

    val beforeFourteenYearsDialogVisiblity = remember {
        mutableStateOf(false)
    }

    val sendVerifyMessage: () -> Unit = {
        passwordErrorMessage.value = ""
        keyboardController?.hide()
        viewmodel.sendVerifyMessage(
            onSuccess = {
                viewmodel.startTimer()
                appState.navController.navigate(SIGNIN_PHONE_VERIFY_ROUTE)
            },
            onError = {
                when (it.code) {
                    "U005" -> passwordErrorMessage.value = it.message
                    else -> {}
                }
                appState.showSnackbar(it.message)
            })
    }

    LaunchedEffect(key1 = Unit) {
        lookupLogEvent("sign_selfish_02")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
    ) {
        AgeDialog(
            initialDialogVisiblity = viewmodel.initialDialogVisiblity.value,
            closeInitialDialog = closeInitialDialog,
            beforeFourteenYearsDialogVisiblity = beforeFourteenYearsDialogVisiblity.value,
            updatebeforeFourteenYearsDialogVisiblity = {
                beforeFourteenYearsDialogVisiblity.value = it
            },
            navigatePopStack = { appState.popBackStack() }
        )

        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.popBackStack()
            }
        }
        OnBoardStep(1)
        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            /** 본인인증 텍스트 */
            OnBoardHeadLine("본인인증", "이 필요해요.")

            /** 이름 입력 */
            NameContainter(
                showBottomSheet = showBottomSheet,
                onFocusRequest = { birthFocusRequest.requestFocus() },
                nameAndNationality = uiState.nameAndNationality,
                updateName = { viewmodel.updateName(it) },
                updateNationality = { viewmodel.updateNationality(it) }
            )
            /** 성별 입력 */
            GenderContainer(
                gender = uiState.gender,
                updateGender = { viewmodel.updateGender(it) }
            )

            /** 생년 입력 */
            BirthContainer(
                focusRequest = birthFocusRequest,
                onFocusRequest = { phoneFocusRequest.requestFocus() },
                birth = uiState.birth,
                updateBirth = { viewmodel.updateBirth(it) }
            )

            /** 휴대전화 입력 */
            PhoneContainer(
                onscrollBottom = { coroutineScope.launch { scrollState.scrollBy(200f) } },
                focusRequest = phoneFocusRequest,
                changeFocus = {
                    isPhoneFocused.value = it
                },
                sendVerifyMessage = {
                    if (uiState.userVerificationStatus) sendVerifyMessage()
                },
                errorMessage = passwordErrorMessage.value,
                showBottomSheet = showBottomSheet,
                phone = uiState.phone,
                updateMobildeCarrier = { viewmodel.updateMobileCarrier(it) },
                updatePhoneNumber = {
                    passwordErrorMessage.value = ""
                    viewmodel.updatePhoneNumber(it)
                }
            )
            HeightSpacer(height = 10.dp)
        }

        if (keyboardState == KeyboardStatus.Closed ||
            (keyboardState == KeyboardStatus.Opened && isPhoneFocused.value)
        ) {
            /** 인증 요청 */
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = uiState.userVerificationStatus,
                colors = ButtonDefaults.buttonColors(
                    if (uiState.userVerificationStatus) Color.Black else Gray300
                ),
                onClick = { sendVerifyMessage() }) {
                Text(
                    modifier = Modifier.padding(0.dp, 5.dp),
                    text = "인증 문자 요청",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun AgeDialog(
    initialDialogVisiblity: Boolean,
    closeInitialDialog: () -> Unit,
    beforeFourteenYearsDialogVisiblity: Boolean,
    updatebeforeFourteenYearsDialogVisiblity: (Boolean) -> Unit,
    navigatePopStack: () -> Unit,
) {
    if (initialDialogVisiblity) {
        RunwayVerticalDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            onDismissRequest = closeInitialDialog,
            title = "만 14세 이상인가요?",
            descrption = "RUNWAY는 만 14세 이사 이용 가능합니다.\n해당 데이터는 저장되지 않으며,\n만 14세 이상임을 증명하는데만 사용됩니다.",
            positiveButton = DialogButtonContent(
                title = "네, 만 14세 이상입니다.",
                onClick = closeInitialDialog
            ),
            negativeButton = DialogButtonContent(title = "아니요, 만 14세 이하입니다.") {
                closeInitialDialog()
                updatebeforeFourteenYearsDialogVisiblity(true)
            }
        )
    }
    if (beforeFourteenYearsDialogVisiblity) {
        RunwayVerticalDialog(
            onDismissRequest = closeInitialDialog,
            title = "만 14세 이상 사용 가능합니다.",
            descrption = "죄송합니다. RUNWAY는 만 14세 이상 사용가능합니다. 우리 나중에 다시 만나요:)",
            positiveButton = DialogButtonContent(title = "안녕, 또 만나요!",
                onClick = { navigatePopStack() }
            ),
        )
    }
}

@Composable
fun TestButon(onClick: () -> Unit) {
    Text(text = "다음화면으로 억지로 이동하기(에러날 수도 있음)",
        modifier = Modifier.clickable {
            onClick()
        })
}


@Composable
fun PhoneContainer(
    changeFocus: (Boolean) -> Unit,
    showBottomSheet: (BottomSheetContent) -> Unit,
    phone: Phone,
    updateMobildeCarrier: (MobileCarrier) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    focusRequest: FocusRequester,
    onscrollBottom: () -> Unit,
    errorMessage: String,
    sendVerifyMessage: () -> Unit,
) {

    val isDropDownMenuExpanded = remember {
        mutableStateOf(false)
    }

    Column {
        Text(text = "휴대폰 인증", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bottomBorder(
                                1.dp, if (isDropDownMenuExpanded.value) Color.Black else Gray600
                            ),
                        onClick = {
                            showBottomSheet(BottomSheetContent(
                                title = "통신사",
                                itemList = MobileCarrier.values()
                                    .map {
                                        BottomSheetContentItem(
                                            itemName = it.getName(),
                                            onItemClick = { updateMobildeCarrier(it) },
                                            isSeleceted = it == phone.mobileCarrier
                                        )
                                    }
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = phone.mobileCarrier.getName(), color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    Icon(
                        tint = Gray400,
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "IC_ARROW",
                        modifier = Modifier
                            .rotate(270f)
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                    )
                }
            }
        }
        HeightSpacer(height = 10.dp)
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            onFocuseChange = changeFocus,
            fontSize = 16.sp,
            focusRequest = focusRequest,
            value = phone.number,
            placeholderText = "휴대폰 번호 입력('-' 제외)",
            onvalueChanged = {
                if (it.length <= PHONE_NUMBER_LENGTH) {
                    updatePhoneNumber(it)
                }
                if (phone.number.isNotBlank() && !phone.checkValidation()) {
                    onscrollBottom()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                sendVerifyMessage()
            }),
            onErrorState = errorMessage.isNotBlank() || (phone.number.isNotBlank() && !phone.checkValidation()),
            errorMessage = errorMessage.ifBlank { "휴대폰번호 11자를 입력해주세요." }
        )

    }
}


@Composable
fun BirthContainer(
    focusRequest: FocusRequester,
    birth: Birth,
    updateBirth: (Birth) -> Unit,
    onFocusRequest: () -> Unit,
) {
    Column {
        Text(text = "생년월일", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            value = birth.date,
            focusRequest = focusRequest,
            placeholderText = "19990101",
            onvalueChanged = {
                if (it.length <= BIRTH_LENGTH) updateBirth(
                    Birth(
                        it
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { onFocusRequest() }),
            onErrorState = birth.date.isNotBlank() && !birth.checkValidation(),
            errorMessage = "생년월일 8자를 입력해주세요."
        )
    }
}


@Composable
private fun GenderContainer(
    gender: Gender,
    updateGender: (Gender) -> Unit,
) {
    Column {
        Text(text = "성별", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        Row(modifier = Modifier.fillMaxWidth()) {
            GenderRadioButton(Gender.Male, gender.isMale()) {
                updateGender(Gender.Male)
            }
            GenderRadioButton(
                Gender.FeMale,
                gender.isFemale()
            ) {
                updateGender(Gender.FeMale)
            }
        }
    }
}

@Composable
private fun RowScope.GenderRadioButton(
    gender: Gender,
    checkGender: Boolean,
    updateGender: () -> Unit,
) {
    val strokeShape = if (gender.isMale()) RoundedCornerShape(5.dp, 0.dp, 0.dp, 5.dp)
    else RoundedCornerShape(0.dp, 5.dp, 5.dp, 0.dp)
    Box(
        modifier = Modifier.Companion
            .weight(1f)
            .border(
                if (checkGender) BorderStroke(1.dp, Primary) else BorderStroke(1.dp, Gray300),
                shape = strokeShape
            )
            .background(if (checkGender) Primary20 else Color.White)
            .clickable {
                updateGender()
            }
    ) {
        Text(
            text = gender.text,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(0.dp, 15.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun NameContainter(
    showBottomSheet: (BottomSheetContent) -> Unit = {},
    nameAndNationality: NameAndNationality = NameAndNationality(
        "",
        Nationality.LOCAL
    ),
    updateName: (String) -> Unit = {},
    updateNationality: (Nationality) -> Unit = {},
    onFocusRequest: () -> Unit = {},
) {
    Column {
        Text(text = "이름", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        Row(verticalAlignment = Alignment.Bottom) {
            CustomTextField(
                modifier = Modifier
                    .weight(3f),
                fontSize = 16.sp,
                value = nameAndNationality.name,
                placeholderText = "이름",
                onvalueChanged = { updateName(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    onFocusRequest()
                }),
            )
            WidthSpacer(width = 15.dp)
            Box(modifier = Modifier.weight(2f)) {
                NationalityButton(showBottomSheet, updateNationality, nameAndNationality)
            }
        }
    }
}

@Composable
private fun NationalityButton(
    showBottomSheet: (BottomSheetContent) -> Unit,
    updateNationality: (Nationality) -> Unit,
    nameAndNationality: NameAndNationality,
) {
    Box(
        modifier = Modifier.padding(0.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .bottomBorder(
                    1.dp,
                    Gray600
                ),
            onClick = {
                showBottomSheet(
                    BottomSheetContent(
                        title = "국가",
                        itemList = Nationality.values().map {
                            BottomSheetContentItem(
                                itemName = it.getString(),
                                onItemClick = { updateNationality(it) },
                                isSeleceted = nameAndNationality.nationality == it
                            )
                        }
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
        }
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = nameAndNationality.nationality.getString(), color = Color.Black,
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
        Icon(
            tint = Gray400,
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "IC_ARROW",
            modifier = Modifier
                .rotate(270f)
                .size(20.dp)
                .align(Alignment.CenterEnd),
        )
    }
}

