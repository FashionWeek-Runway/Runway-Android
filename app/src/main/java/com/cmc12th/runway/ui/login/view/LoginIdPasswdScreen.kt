package com.cmc12th.runway.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.LastPasswordVisibleCustomTextField
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.signin.model.Phone
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Gray600
import com.cmc12th.runway.ui.theme.HeadLine1
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH

@Composable
fun LoginIdPasswdScreen(
    appState: ApplicationState,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    val onkeyboardState by keyboardAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        /** 상단 뒤로가기 */
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }

        /** 로그인 본문 */
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 60.dp)
                .weight(1f),
        ) {
            Text(text = "로그인", style = HeadLine1)

            /** 핸드폰 텍스트 필드 */
            HeightSpacer(60.dp)
            PhoneTextField(loginViewModel.phoneNumber.value, loginViewModel::updatePhoneNumber)

            /** 비밀번호 텍스트 필드 */
            HeightSpacer(30.dp)
            PasswordTextField(loginViewModel.password.value,
                loginViewModel::updatePassword,
                onDone = {
                    appState.navController.navigate(MAIN_GRAPH)
                })

            HeightSpacer(30.dp)
            Text(text = "비밀번호 찾기", style = Body2, modifier = Modifier.align(Alignment.End))
        }

        /** 하단 로그인, 회원가입 */
        BottomButtons(
            onkeyboardState == KeyboardStatus.Opened,
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
private fun BottomButtons(
    onLoginFocused: Boolean,
    onLogin: () -> Unit,
    onSignin: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(bottom = if (onLoginFocused) 0.dp else 30.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (!onLoginFocused) 20.dp else 0.dp,
                    vertical = if (!onLoginFocused) 10.dp else 0.dp
                )
                .clip(shape = RoundedCornerShape(if (!onLoginFocused) 5.dp else 0.dp))
                .background(Color.Black),
            onClick = onLogin,
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(text = "로그인", color = Color.White, fontSize = 16.sp)
        }
        if (!onLoginFocused) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
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
}

@Composable
private fun PhoneTextField(
    phone: Phone,
    updatePhoneNumber: (String) -> Unit,
) {
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 16.sp,
        value = phone.number,
        placeholderText = "전화번호 입력",
        onvalueChanged = updatePhoneNumber,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onDone = {
        }),
    )
}

@Composable
private fun PasswordTextField(
    password: Password,
    updatePassword: (String) -> Unit,
    onDone: () -> Unit,
) {
    LastPasswordVisibleCustomTextField(
        value = password.value, onvalueChanged = updatePassword,
        modifier = Modifier
            .fillMaxWidth(),
        placeholderText = "비밀번호 입력",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        }),
    )
}


