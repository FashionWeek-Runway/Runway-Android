package com.cmc12th.runway.ui.login.signin

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.model.MobileCarrier
import com.cmc12th.runway.ui.theme.*

@Composable
fun SignInUserInfoVerifyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(1)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            /** 본인인증 텍스트 */
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text(text = "본인인증", style = HeadLine3)
                Text(text = "이 필요해요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }

            /** 이름 입력 */
            NameContainter()

            /** 성별 입력 */
            GenderContainer()

            /** 생년 입력 */
            BirthContainer()

            /** 휴대전화 입력 */
            PhoneContainer()

            /** 인증 요청 */
            Button(modifier = Modifier
                .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Gray300), onClick = { /*TODO*/ }) {
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
fun PhoneContainer() {
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
                        onClick = { isDropDownMenuExpanded.value = true },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = phoneCategory.value.getName(), color = Color.Black,
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

                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp),
                    expanded = isDropDownMenuExpanded.value,
                    onDismissRequest = { !isDropDownMenuExpanded.value }) {
                    MobileCarrier.values().forEach {
                        DropdownMenuItem(
                            text = { Text(modifier = Modifier, text = it.getName()) },
                            onClick = {
                                phoneCategory.value = it
                                isDropDownMenuExpanded.value = false
                            })
                    }
                }
            }
            HeightSpacer(height = 10.dp)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                value = phoneNumber.value,
                placeholderText = "휴대폰 번호 입력('-' 제외)",
                onvalueChanged = { if (it.text.length <= 11) phoneNumber.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onDone = {
                }),
            )
        }
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
                    .background(if (genderValue.value == "Male") Primary_20 else Color.White)
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
                    .background(if (genderValue.value == "Female") Primary_20 else Color.White)
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
private fun NameContainter() {
    // TODO 뷰모델로 추출
    val nameTextField = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val isDropDownMenuExpanded = remember {
        mutableStateOf(false)
    }
    val countryTextField = remember {
        mutableStateOf(TextFieldValue("내국인"))
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
                                if (isDropDownMenuExpanded.value) Color.Black else Gray600
                            ),
                        onClick = { isDropDownMenuExpanded.value = true },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = countryTextField.value.text, color = Color.Black,
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

                DropdownMenu(
                    modifier = Modifier.wrapContentSize(),
                    expanded = isDropDownMenuExpanded.value,
                    onDismissRequest = { !isDropDownMenuExpanded.value }) {
                    DropdownMenuItem(
                        text = { Text(text = "내국인") },
                        onClick = {
                            countryTextField.value = TextFieldValue("내국인")
                            isDropDownMenuExpanded.value = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = "외국인") },
                        onClick = {
                            countryTextField.value = TextFieldValue("외국인")
                            isDropDownMenuExpanded.value = false
                        })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInUserVerificationScreenPreview() {
    SignInUserInfoVerifyScreen()
}