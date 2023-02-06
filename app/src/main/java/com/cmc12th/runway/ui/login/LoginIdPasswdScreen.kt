package com.cmc12th.runway.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Gray600
import com.cmc12th.runway.ui.theme.HeadLine1
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH

@Composable
fun LoginIdPasswdScreen(appState: ApplicationState) {

    // TODO ViewModel로 추출
    val phoneTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val passWdTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        /** 상단 뒤로가기 */
        BackIcon()

        /** 로그인 본문 */
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
        ) {
            Text(text = "로그인", style = HeadLine1)
            HeightSpacer(60.dp)
            PhoneTextField(phoneTextFieldValue)
            // Text(text = "error Message", color = Color.Red)
            HeightSpacer(30.dp)
            PasswordTextField(passWdTextFieldValue)
            // Text(text = "error Message", color = Color.Red)
            HeightSpacer(30.dp)
            Text(text = "비밀번호 찾기", style = Body2, modifier = Modifier.align(Alignment.End))
        }

        /** 하단 로그인, 회원가입 */
        BottomButtons(
            onLogin = {
                appState.navController.navigate(MAIN_GRAPH)
            },
            onSignin = {
                appState.navController.navigate(SIGNIN_GRAPH)
            }
        )
    }
}

@Composable
private fun PhoneTextField(phoneTextFieldValue: MutableState<TextFieldValue>) {

    CustomTextField(
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 16.sp,
        value = phoneTextFieldValue.value,
        placeholderText = "전화번호 입력",
        onvalueChanged = { phoneTextFieldValue.value = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onDone = {
        }),
    )
}

@Composable
private fun PasswordTextField(passWdTextFieldValue: MutableState<TextFieldValue>) {
    val pswdVisible = remember {
        mutableStateOf(false)
    }
    CustomTextField(
        trailingIcon = {
            if (pswdVisible.value) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_able_pw),
                    tint = Gray600,
                    contentDescription = "IC_ABLE_PW",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            pswdVisible.value = !pswdVisible.value
                        },
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_disable_pw),
                    tint = Color.Unspecified,
                    contentDescription = "IC_DISABLE_PW",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            pswdVisible.value = !pswdVisible.value
                        },
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 16.sp,
        value = passWdTextFieldValue.value,
        placeholderText = "비밀번호 입력",
        onvalueChanged = { passWdTextFieldValue.value = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
        }),
        passwordVisible = pswdVisible.value
    )
}


@Composable
private fun BottomButtons(onLogin: () -> Unit, onSignin: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(bottom = 30.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.Black),
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(Color.Black)
        )
        {
            Text(text = "로그인", color = Color.White, fontSize = 16.sp)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.White)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(5.dp)),
            onClick = onSignin,
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(text = "회원가입", color = Color.Black, fontSize = 16.sp)
        }
    }
}

