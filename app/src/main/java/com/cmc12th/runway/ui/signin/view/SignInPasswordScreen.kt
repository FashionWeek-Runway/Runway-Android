@file:OptIn(ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.LastPasswordVisibleCustomTextField
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.domain.model.signin.Password
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants

@Composable
fun SignInPasswordScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by signInViewModel.passwordUiState.collectAsStateWithLifecycle()
    val (retryFocusRequest) = remember { FocusRequester.createRefs() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding() // 버튼 같이 올라오게 하기 위해서
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.navController.popBackStack()
            }
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
            HeightSpacer(height = 13.dp)
            InputPassword(
                requestFocus = { retryFocusRequest.requestFocus() },
                password = uiState.password,
                updatePassword = { signInViewModel.updatePassword(it) }
            )
            /** 패스워드 확인 */
            HeightSpacer(height = 10.dp)
            CheckPassword(
                password = uiState.retryPassword,
                onDone = { appState.navController.navigate(Constants.SIGNIN_AGREEMENT_ROUTE) },
                updateRetryPassword = { signInViewModel.updateRetryPassword(it) },
                isEqual = uiState.checkValidate(),
                focusRequest = retryFocusRequest,
            )
        }
        Button(
            onClick = {
                appState.navController.navigate(Constants.SIGNIN_AGREEMENT_ROUTE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = uiState.checkValidate(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                if (uiState.checkValidate()) Color.Black else Gray300
            )
        ) {
            Text(
                text = "다음",
                modifier = Modifier.padding(0.dp, 5.dp),
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
    focusRequest: FocusRequester,
    onDone: () -> Unit,
) {

    Column {
        LastPasswordVisibleCustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 16.sp,
            value = password.value,
            placeholderText = "비밀번호 확인",
            focusRequest = focusRequest,
            onvalueChanged = { updateRetryPassword(Password(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (isEqual) onDone() }
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
    requestFocus: () -> Unit,
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
                onNext = { requestFocus() }
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
        Row {
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

