@file:OptIn(ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.broadcast.SystemBroadcastReceiver
import com.cmc12th.runway.broadcast.parseSmsMessage
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.signin.components.RetryContainer
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.SIGNIN_PASSWORD_ROUTE

@Composable
fun SignInPhoneVerifyScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by signInViewModel.phoneVerifyUiState.collectAsStateWithLifecycle()
    val verifyErrorMessage = remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val verifyPhoneNumber: () -> Unit = {
        keyboardController?.hide()
        signInViewModel.verifyPhoneNumber(
            onSuccess = { appState.navController.navigate(SIGNIN_PASSWORD_ROUTE) },
            onError = {
                appState.showSnackbar(it.message)
                verifyErrorMessage.value = it.message
            }
        )
    }

    SystemBroadcastReceiver(Constants.SNS_INTENT_ACTION) { intent ->
        val bundle = intent?.extras ?: return@SystemBroadcastReceiver // 번들이 없을때는 아무일도 없었다 시전
        val messages = parseSmsMessage(bundle)
        if (messages.isNotEmpty()) {
            // 문자메세지 내용 추출
            val contents = messages[0]?.messageBody.toString()
            val regex = Regex("""[^0-9]""")
            signInViewModel.updateVerifyCode(contents.replace(regex, ""))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.navController.popBackStack()
            }
        }
        OnBoardStep(2)
//        TestButon {
//            appState.navController.navigate(SIGNIN_PASSWORD_ROUTE)
//        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            HeightSpacer(height = 20.dp)
            OnBoardHeadLine(main = "인증번호", sub = "를 입력해주세요.")
            HeightSpacer(height = 20.dp)
            Row {
                Text(text = "인증문자가 ", color = Gray700, style = Body2)
                Text(text = uiState.phone.number, color = Color.Black, style = Body2M)
                Text(text = "으로 발송되었습니다.", color = Gray700, style = Body2)
            }
            HeightSpacer(height = 40.dp)

            /** 인증번호 입력 */
            InputVerificationCode(
                retryTime = uiState.retryTime,
                verifyErrorMessage = verifyErrorMessage.value,
                verifyCode = uiState.verifyCode,
                resendMessage = {
                    keyboardController?.hide()
                    signInViewModel.sendVerifyMessage(
                        onSuccess = {
                            signInViewModel.resetTimer()
                            appState.showSnackbar("인증번호를 재요청 했습니다.")
                        },
                        onError = {
                            appState.showSnackbar(it.message)
                        }
                    )
                },
                verifyPhonNumber = { if (uiState.verifyCode.length == 6) verifyPhoneNumber() },
                updateVerifyCode = { signInViewModel.updateVerifyCode(it) }
            )
        }
        Button(
            onClick = verifyPhoneNumber,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = uiState.verifyCode.length == 6,
            colors = ButtonDefaults.buttonColors(if (uiState.verifyCode.length == 6) Color.Black else Gray300)
        ) {
            Text(
                text = "인증 확인",
                modifier = Modifier.padding(0.dp, 5.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun InputVerificationCode(
    verifyCode: String,
    updateVerifyCode: (String) -> Unit,
    verifyErrorMessage: String,
    resendMessage: () -> Unit,
    retryTime: Int,
    verifyPhonNumber: () -> Unit,
) {

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        Box(Modifier.weight(1f)) {
            CustomTextField(
                trailingIcon = {
                    RetryContainer(retryTime = retryTime, resendMessage = resendMessage)
                },
                modifier = Modifier,
                fontSize = 16.sp,
                focusRequest = focusRequester,
                value = verifyCode,
                placeholderText = "숫자 6자리 입력",
                onvalueChanged = { if (it.length <= 6) updateVerifyCode(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    verifyPhonNumber()
                }),
                onErrorState = verifyErrorMessage.isNotBlank(),
                errorMessage = verifyErrorMessage
            )
        }
    }
}


