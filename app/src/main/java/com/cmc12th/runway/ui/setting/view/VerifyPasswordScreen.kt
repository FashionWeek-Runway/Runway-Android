@file:OptIn(ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.setting.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.LastPasswordVisibleCustomTextField
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.setting.SettingViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.SETTING_EDIT_PASSWORD_ROUTE

@Composable
fun VerifyPasswordScreen(
    appState: ApplicationState,
    viewModel: SettingViewModel
) {
    val uiState by viewModel.editPasswordUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    val verifyPassword = {
        viewModel.verifyPassword(
            onSuccess = {
                appState.navigate(SETTING_EDIT_PASSWORD_ROUTE)
            },
            onError = {
                appState.showSnackbar(it.message)
            })
    }

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding() // 버튼 같이 올라오게 하기 위해서
    ) {

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
            OnBoardHeadLine(main = "기존 비밀번호", sub = "를 입력해주세요.")

            HeightSpacer(height = 30.dp)
            /** 패스워드 입력 */
            Column(modifier = Modifier.weight(1f)) {
                LastPasswordVisibleCustomTextField(
                    focusRequest = focusRequester,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    value = uiState.verifyPassword.value,
                    placeholderText = "비밀번호 입력",
                    onvalueChanged = { viewModel.updateVerifyPassword(Password(it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.verifyPassword.inLegnth()) verifyPassword()
                        }
                    )
                )
            }
            Button(
                onClick = {
                    keyboardController?.hide()
                    verifyPassword()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = uiState.verifyPassword.inLegnth(),
                colors = ButtonDefaults.buttonColors(
                    if (uiState.verifyPassword.inLegnth()) Color.Black else Gray300
                )
            ) {
                Text(
                    text = "확인",
                    modifier = Modifier.padding(0.dp, 5.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

