@file:OptIn(ExperimentalComposeUiApi::class)

package com.cmc12th.runway.ui.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.LastPasswordVisibleCustomTextField
import com.cmc12th.runway.ui.domain.keyboardAsState
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.LoginViewModel
import com.cmc12th.domain.model.signin.model.Password
import com.cmc12th.domain.model.signin.model.Phone
import com.cmc12th.runway.ui.signin.view.ErrorMessage
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH

@Composable
fun LoginIdPasswdScreen(
    appState: ApplicationState,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val loginIdPasswordUiState by loginViewModel.loginIdPasswordUiState.collectAsStateWithLifecycle()

    val phoneErrorMessage = remember {
        mutableStateOf("")
    }
    val passwordErrorMessage = remember {
        mutableStateOf("")
    }
    val navigateToPasswrodSearch = {
        appState.navigate(Constants.PASSWORD_SEARCH_GRAPH)
    }

    val onLogin: () -> Unit = {
        keyboardController?.hide()
        loginViewModel.login(
            onSuccess = {
                appState.navController.navigate(MAIN_GRAPH) {
                    popUpTo(LOGIN_GRAPH)
                }
            },
            onError = {
                when (it.code) {
                    "U001" -> phoneErrorMessage.value = it.message
                    "U002" -> passwordErrorMessage.value = it.message
                    "U021" -> phoneErrorMessage.value = it.message
                    "U010" -> passwordErrorMessage.value = it.message
                }
                appState.showSnackbar(it.message)
            }
        )
    }

    val onSignin = {
        appState.navController.navigate(SIGNIN_GRAPH)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        /** 상단 뒤로가기 */
        Row(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "로그인", style = Body1B)
        }
        if (passwordErrorMessage.value.isNotBlank() || phoneErrorMessage.value.isNotBlank()) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                ErrorMessage(message = passwordErrorMessage.value + phoneErrorMessage.value)
            }
        }

        HeightSpacer(height = 20.dp)
        /** 로그인 본문 */
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        ) {

            /** 핸드폰 텍스트 필드 */
            PhoneTextField(
                phone = loginIdPasswordUiState.phoneNumber,
                phoneErrorMessage = phoneErrorMessage.value,
                updatePhoneNumber = {
                    phoneErrorMessage.value = ""
                    passwordErrorMessage.value = ""
                    loginViewModel.updatePhoneNumber(it)
                })

            /** 비밀번호 텍스트 필드 */
            HeightSpacer(20.dp)
            PasswordTextField(
                password = loginIdPasswordUiState.password,
                errorState = passwordErrorMessage.value.isNotBlank(),
                updatePassword = {
                    passwordErrorMessage.value = ""
                    phoneErrorMessage.value = ""
                    loginViewModel.updatePassword(it)
                },
                onDone = onLogin
            )


            HeightSpacer(20.dp)
            /** 하단 로그인, 회원가입 */
            BottomButtons(onLogin = onLogin)

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "비밀번호 찾기",
                    style = Body2,
                    modifier = Modifier
                        .clickable { navigateToPasswrodSearch() }
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .padding(0.dp, 2.dp)
                        .background(Gray200)
                )
                Text(
                    text = "회원가입",
                    style = Body2,
                    modifier = Modifier
                        .clickable { onSignin() }
                )
            }
        }
    }
}

@Composable
private fun BottomButtons(
    onLogin: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(Color.Black),
        shape = RoundedCornerShape(4.dp),
        onClick = onLogin,
        colors = ButtonDefaults.buttonColors(Color.Black)
    ) {
        Text(text = "로그인", color = Color.White, fontSize = 16.sp)
    }

}

@Composable
private fun PhoneTextField(
    phone: com.cmc12th.domain.model.signin.model.Phone,
    updatePhoneNumber: (String) -> Unit,
    phoneErrorMessage: String,
) {
    CustomTextField(
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 16.sp,
        value = phone.number,
        placeholderText = "전화번호 입력",
        onvalueChanged = {
            if (it.length <= com.cmc12th.domain.model.signin.model.Phone.PHONE_NUMBER_LENGTH) {
                updatePhoneNumber(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onDone = {
        }),
        onErrorState = phoneErrorMessage.isNotBlank(),
//        errorMessage = phoneErrorMessage
    )
}

@Composable
private fun PasswordTextField(
    password: com.cmc12th.domain.model.signin.model.Password,
    updatePassword: (String) -> Unit,
    onDone: () -> Unit,
    errorState: Boolean,
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
        onErrorState = errorState
    )
}


