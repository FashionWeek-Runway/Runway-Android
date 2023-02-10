@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.*
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.*
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.ui.signin.model.Birth.Companion.BIRTH_LENGTH
import com.cmc12th.runway.ui.signin.model.Phone.Companion.PHONE_NUMBER_LENGTH
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_PHONE_VERIFY_ROUTE
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
            bottomsheetState.modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }

    CustomBottomSheet(
        bottomsheetState
    ) {
        UserVerificationContents(
            appState,
            showBottomSheet,
            signInViewModel,
        )
    }
}

@Composable
private fun UserVerificationContents(
    appState: ApplicationState,
    showBottomSheet: (BottomSheetContent) -> Unit,
    viewmodel: SignInViewModel,
) {
    val keyboardState by keyboardAsState()

    /** 핸드폰 TextField가 포커스일 때 로그인 버튼이 올라오게 하기위해 */
    val isPhoneFocused = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(1)

        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            /** 본인인증 텍스트 */
            OnBoardHeadLine("본인인증", "이 필요해요.")

            /** 이름 입력 */
            NameContainter(
                showBottomSheet,
                nameAndNationality = viewmodel.nameAndNationality.value,
                updateName = { viewmodel.updateName(it) },
                updateNationality = { viewmodel.updateNationality(it) }
            )
            /** 성별 입력 */
            GenderContainer(
                gender = viewmodel.gender.value,
                updateGender = { viewmodel.updateGender(it) }
            )

            /** 생년 입력 */
            BirthContainer(
                birth = viewmodel.birth.value,
                updateBirth = { viewmodel.updateBirth(it) }
            )

            /** 휴대전화 입력 */
            PhoneContainer(
                changeFocus = { isPhoneFocused.value = it },
                showBottomSheet = showBottomSheet,
                phone = viewmodel.phone.value,
                updateMobildeCarrier = { viewmodel.updateMobileCarrier(it) },
                updatePhoneNumber = { viewmodel.updatePhoneNumber(it) }
            )

        }

        if (keyboardState == KeyboardStatus.Closed ||
            (keyboardState == KeyboardStatus.Opened && isPhoneFocused.value)
        ) {
            /** 인증 요청 */
            Button(modifier = Modifier
                .fillMaxWidth(),
                shape = RectangleShape,
//                enabled = viewmodel.userVerificationStatus.value,
                colors = ButtonDefaults.buttonColors(
                    if (viewmodel.userVerificationStatus.value) Color.Black else Gray300
                ),
                onClick = {
                    appState.navController.navigate(SIGNIN_PHONE_VERIFY_ROUTE)
                }) {
                Text(
                    modifier = Modifier.padding(0.dp, 5.dp),
                    text = "인증 요청",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun PhoneContainer(
    changeFocus: (Boolean) -> Unit,
    showBottomSheet: (BottomSheetContent) -> Unit,
    phone: Phone,
    updateMobildeCarrier: (MobileCarrier) -> Unit,
    updatePhoneNumber: (String) -> Unit,
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
                                itemList = MobileCarrier.values().map {
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
            value = phone.number,
            placeholderText = "휴대폰 번호 입력('-' 제외)",
            onvalueChanged = { if (it.length <= PHONE_NUMBER_LENGTH) updatePhoneNumber(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
            }),
            onErrorState = phone.number.isNotBlank() && !phone.checkValidation(),
            errorMessage = "휴대폰번호 11자를 입력해주세요."
        )
    }
}


@Composable
fun BirthContainer(
    birth: Birth,
    updateBirth: (Birth) -> Unit,
) {
    Column {
        Text(text = "생년월일", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            value = birth.date,
            placeholderText = "19990101",
            onvalueChanged = { if (it.length <= BIRTH_LENGTH) updateBirth(Birth(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
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
            GenderRadioButton(Gender.FeMale, gender.isFemale()) {
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
            text = gender.getText(),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(0.dp, 15.dp)
        )
    }
}

@Composable
private fun NameContainter(
    showBottomSheet: (BottomSheetContent) -> Unit,
    nameAndNationality: NameAndNationality,
    updateName: (String) -> Unit,
    updateNationality: (Nationality) -> Unit,
) {
    Column {
        Text(text = "이름", style = Caption, color = Gray700)
        HeightSpacer(height = 10.dp)
        Row(verticalAlignment = Alignment.Top) {
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
                keyboardActions = KeyboardActions(onDone = {
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
        modifier = Modifier.padding(0.dp, 4.dp)
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

