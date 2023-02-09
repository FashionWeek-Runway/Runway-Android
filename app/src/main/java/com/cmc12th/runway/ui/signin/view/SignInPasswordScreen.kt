package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.LastPasswordVisibleCustomTextField
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants

@Composable
fun SignInPasswordScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding() // 버튼 같이 올라오게 하기 위해서
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(3)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            HeightSpacer(height = 20.dp)
            OnBoardHeadLine(main = "비밀번호", sub = "를 입력해주세요.")

            /** 패스워드 입력 */
            HeightSpacer(height = 30.dp)
            InputPassword(
                password = signInViewModel.password.value,
                updatePassword = { signInViewModel.updatePassword(it) }
            )
            /** 패스워드 확인 */
            HeightSpacer(height = 30.dp)
            CheckPassword(
                password = signInViewModel.retryPassword.value,
                updateRetryPassword = { signInViewModel.updateRetryPassword(it) },
                isEqual = signInViewModel.password.value.isValidatePassword(
                    signInViewModel.retryPassword.value
                )
            )
        }
        Button(
            onClick = {
                appState.navController.navigate(Constants.SIGNIN_AGREEMENT_ROUTE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                if (signInViewModel.password.value.isValidatePassword(
                        signInViewModel.retryPassword.value
                    )
                ) Color.Black else Gray300
            )
        ) {
            Text(
                text = "다음",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CheckPassword(
    password: Password,
    updateRetryPassword: (Password) -> Unit,
    isEqual: Boolean,
) {

    Column {
        LastPasswordVisibleCustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 16.sp,
            value = password.value,
            placeholderText = "비밀번호 확인",
            onvalueChanged = { updateRetryPassword(Password(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {}
            )
        )

        HeightSpacer(height = 10.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            passwdValidationIcon("비밀번호 일치", isEqual)
        }
    }
}

@Composable
fun InputPassword(
    password: Password,
    updatePassword: (Password) -> Unit,
) {

    val focusRequester = remember {
        FocusRequester()
    }


    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column {
        LastPasswordVisibleCustomTextField(
            focusRequest = focusRequester,
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 16.sp,
            value = password.value,
            placeholderText = "비밀번호 입력",
            onvalueChanged = { updatePassword(Password(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
            )
        )

        HeightSpacer(height = 10.dp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            passwdValidationIcon("영문", password.includeEnglish())
            passwdValidationIcon("숫자", password.includeNumber())
            passwdValidationIcon("8~16자", password.inLegnth())
        }
    }
}

@Composable
private fun passwdValidationIcon(text: String, status: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row() {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "IC_CHECK",
                tint = if (status) Primary else Gray500
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                style = Body2M,
                color = if (status) Primary else Gray500
            )
        }
    }
}

