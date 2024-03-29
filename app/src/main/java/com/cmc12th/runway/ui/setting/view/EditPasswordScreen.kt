@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.setting.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayVerticalDialog
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.setting.SettingViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.view.CheckPassword
import com.cmc12th.runway.ui.signin.view.InputPassword
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SETTING_GRAPH

@Composable
fun EditPasswordScreen(
    appState: ApplicationState,
    viewModel: SettingViewModel
) {

    val uiState by viewModel.editPasswordUiState.collectAsStateWithLifecycle()
    val (retryFocusRequest) = remember { FocusRequester.createRefs() }
    val completeDialogVisiblitiy = remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val changePassword = {
        viewModel.modifyPassword(
            onSuccess = {
                completeDialogVisiblitiy.value = true
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding() // 버튼 같이 올라오게 하기 위해서
    ) {

        CompleteDialog(
            onDismiss = {
                appState.navController.navigate(MAIN_GRAPH) {
                    popUpTo(SETTING_GRAPH) {
                        inclusive = true
                    }
                }
            },
            completeDialogVisiblitiy = completeDialogVisiblitiy.value,
        )

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "비밀번호 변경", style = Body1B, color = Color.Black)
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
                password = uiState.newPassword,
                updatePassword = {
                    viewModel.updateNewPassword(it)
                }
            )
            /** 패스워드 확인 */
            HeightSpacer(height = 10.dp)
            CheckPassword(
                password = uiState.checkNewPassword,
                onDone = {
                    keyboardController?.hide()
                    changePassword()
                },
                updateRetryPassword = { viewModel.updateCheckNewPassword(it) },
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
                .padding(20.dp, 10.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = uiState.checkValidate(),
            colors = ButtonDefaults.buttonColors(
                if (uiState.checkValidate()) Color.Black else Gray300
            )
        ) {
            Text(
                text = "비밀번호 변경",
                modifier = Modifier.padding(0.dp, 5.dp),
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
    onDismiss: () -> Unit,
) {
    if (completeDialogVisiblitiy) {
        RunwayVerticalDialog(
            properties = DialogProperties(dismissOnClickOutside = false),
            onDismissRequest = {
                onDismiss()
            },
            title = "비밀번호 변경 완료!",
            descrption = "비밀번호가 변경되었습니다.\n새로운 비밀번호로 로그인 해주세요.",
            positiveButton = DialogButtonContent(
                title = "확인",
                onClick = {
                    onDismiss()
                }
            ),
        )
    }
}