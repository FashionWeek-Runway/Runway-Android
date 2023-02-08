@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.signin

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.model.*
import com.cmc12th.runway.ui.domain.model.BottomSheetState
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_PHONE_VERIFY_ROUTE
import kotlinx.coroutines.launch

@Composable
fun rememberBottomSheet(
    modalSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    ),
    bottomsheetContent: MutableState<BottomSheetContent> = mutableStateOf(BottomSheetContent()),
) = remember(modalSheetState, bottomsheetContent) {
    BottomSheetState(
        modalSheetState,
        bottomsheetContent
    )
}

@Composable
fun SignInUserInfoVerifyScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val bottomsheetState = rememberBottomSheet()
    val showBottomSheet: (BottomSheetContent) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }

    // TODO ViewModel 추출
    val userMobileCarrier = remember {
        mutableStateOf(MobileCarrier.SKT)
    }
    val userNationality = remember {
        mutableStateOf(Nationality.LOCAL)
    }
    CustomBottomSheet(
        bottomsheetState
    ) {
        UserVerificationContents(appState, showBottomSheet, userMobileCarrier, userNationality)
    }
}

@Composable
private fun UserVerificationContents(
    appState: ApplicationState,
    showBottomSheet: (BottomSheetContent) -> Unit,
    selectedMobileCarrier: MutableState<MobileCarrier>,
    userNationality: MutableState<Nationality>,
) {

    val keyboardState by keyboardAsState()
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
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "본인인증", style = HeadLine3)
                Text(text = "이 필요해요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }

            /** 이름 입력 */
            NameContainter(
                showBottomSheet,
                userNationality.value
            ) {
                userNationality.value = it
            }

            /** 성별 입력 */
            GenderContainer()

            /** 생년 입력 */
            BirthContainer()

            /** 휴대전화 입력 */
            PhoneContainer(
                { isPhoneFocused.value = it },
                showBottomSheet,
                selectedMobileCarrier.value
            ) { selectedMobileCarrier.value = it }

        }

        if (keyboardState == Keyboard.Closed ||
            (keyboardState == Keyboard.Opened && isPhoneFocused.value)
        ) {
            /** 인증 요청 */
            Button(modifier = Modifier
                .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Gray300),
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
    selectedMobileCarrier: MobileCarrier,
    updateMobildeCarrier: (MobileCarrier) -> Unit,
) {
    // TODO ViewModel로 추출
    val phoneCategory = remember {
        mutableStateOf(MobileCarrier.SKT)
    }
    val phoneNumber = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val isDropDownMenuExpanded = remember {
        mutableStateOf(false)
    }

    Column {
        Text(text = "휴대폰 인증", fontSize = 12.sp, color = Gray700)
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
                                        isSeleceted = it == selectedMobileCarrier
                                    )
                                }
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = selectedMobileCarrier.getName(), color = Color.Black,
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
                .fillMaxWidth()
                .onFocusChanged {
                    changeFocus(it.hasFocus)
                },
            fontSize = 16.sp,
            value = phoneNumber.value,
            placeholderText = "휴대폰 번호 입력('-' 제외)",
            onvalueChanged = { if (it.text.length <= 11) phoneNumber.value = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
            }),
        )
    }
}


@Composable
fun BirthContainer() {
    // TODO ViewModel로 추출
    val birthValue = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column {
        Text(text = "생년월일", fontSize = 12.sp, color = Gray700)
        HeightSpacer(height = 10.dp)
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            value = birthValue.value,
            placeholderText = "19990101",
            onvalueChanged = { if (it.text.length <= 8) birthValue.value = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = {
            }),
        )
    }
}

@Composable
private fun GenderContainer() {
    // TODO 뷰모델로 추출
    val genderValue = remember {
        mutableStateOf("")
    }

    Column {
        Text(text = "성별", fontSize = 12.sp, color = Gray700)
        HeightSpacer(height = 10.dp)
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        if (genderValue.value == "Male") BorderStroke(
                            1.dp,
                            Primary
                        ) else BorderStroke(1.dp, Gray300)
                    )
                    .background(if (genderValue.value == "Male") Primary20 else Color.White)
                    .clickable {
                        genderValue.value = "Male"
                    }
            ) {
                Text(
                    text = "남",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(0.dp, 15.dp)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        if (genderValue.value == "Female") BorderStroke(
                            1.dp,
                            Primary
                        ) else BorderStroke(1.dp, Gray300)
                    )
                    .background(if (genderValue.value == "Female") Primary20 else Color.White)
                    .clickable {
                        genderValue.value = "Female"
                    }
            ) {
                Text(
                    text = "여", fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(0.dp, 15.dp)
                )
            }
        }
    }
}

@Composable
private fun NameContainter(
    showBottomSheet: (BottomSheetContent) -> Unit,
    userNationality: Nationality,
    changeUserNationality: (Nationality) -> Unit,
) {
    // TODO 뷰모델로 추출
    val nameTextField = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column {
        Text(text = "이름", fontSize = 12.sp, color = Gray700)
        HeightSpacer(height = 10.dp)
        Row(verticalAlignment = Alignment.Bottom) {
            CustomTextField(
                modifier = Modifier
                    .weight(3f),
                fontSize = 16.sp,
                value = nameTextField.value,
                placeholderText = "이름",
                onvalueChanged = { nameTextField.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = {
                }),
            )
            WidthSpacer(width = 15.dp)
            Box(modifier = Modifier.weight(2f)) {
                Box {
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
                                            onItemClick = { changeUserNationality(it) },
                                            isSeleceted = userNationality == it
                                        )
                                    }
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = userNationality.getString(), color = Color.Black,
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
    }
}

