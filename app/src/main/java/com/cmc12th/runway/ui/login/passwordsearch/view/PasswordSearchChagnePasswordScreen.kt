@file:OptIn(ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.login.passwordsearch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.login.passwordsearch.PasswordSearchViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.view.CheckPassword
import com.cmc12th.runway.ui.signin.view.InputPassword
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import kotlinx.coroutines.Job

@Composable
fun PasswordSearchChagnePasswordScreen(
    appState: ApplicationState,
    passwordSearchViewModel: PasswordSearchViewModel
) {

    val uiState by passwordSearchViewModel.passwordUiState.collectAsStateWithLifecycle()
    val (retryFocusRequest) = remember { FocusRequester.createRefs() }

    val completeDialogVisiblitiy = remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val changePassword = {
        passwordSearchViewModel.modifyPassword(
            onSuccess = {
                appState.navigate(LOGIN_ID_PW_ROUTE)
            },
            onError = {
                appState.showSnackbar(it.message)
            })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding() // 버튼 같이 올라오게 하기 위해서
    ) {

        CompleteDialog(
            appState = appState,
            completeDialogVisiblitiy = completeDialogVisiblitiy.value,
        )

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "비밀번호 찾기", style = Body1B, color = Color.Black)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            HeightSpacer(height = 20.dp)
            OnBoardHeadLine(main = "새로운 비밀번호", sub = "를 입력해주세요.")

            /** 패스워드 입력 */
            HeightSpacer(height = 30.dp)
            InputPassword(
                requestFocus = { retryFocusRequest.requestFocus() },
                password = uiState.password,
                updatePassword = { passwordSearchViewModel.updatePassword(it) }
            )
            /** 패스워드 확인 */
            HeightSpacer(height = 30.dp)
            CheckPassword(
                password = uiState.retryPassword,
                onDone = {
                    keyboardController?.hide()
                    changePassword()
                },
                updateRetryPassword = { passwordSearchViewModel.updateRetryPassword(it) },
                isEqual = uiState.checkValidate(),
                focusRequest = retryFocusRequest,
            )
        }
        Button(
            onClick = {
                keyboardController?.hide()
                changePassword()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = uiState.checkValidate(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                if (uiState.checkValidate()) Color.Black else Gray300
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
private fun CompleteDialog(
    completeDialogVisiblitiy: Boolean,
    appState: ApplicationState
) {
    if (completeDialogVisiblitiy) {
        RunwayDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            onDismissRequest = {
                appState.navigate(LOGIN_ID_PW_ROUTE)
            },
            title = "비밀번호 변경 완료!",
            descrption = "비밀번호가 변경되었습니다.\n새로운 비밀번호로 로그인 해주세요.",
            positiveButton = DialogButtonContent(
                title = "확인",
                onClick = {
                    appState.navigate(LOGIN_ID_PW_ROUTE)
                }
            ),
        )
    }
}

