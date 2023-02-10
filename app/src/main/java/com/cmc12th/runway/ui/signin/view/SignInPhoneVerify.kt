@file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_PASSWORD_ROUTE
import kotlin.math.sign

@Composable
fun SignInPhoneVerifyScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by signInViewModel.phoneVerifyUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(2)

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
                Text(text = "signInViewModel.value.number", color = Color.Black, style = Body2M)
                Text(text = "으로 발송되었습니다.", color = Gray700, style = Body2)
            }
            HeightSpacer(height = 40.dp)
            /** 인증번호 입력 */
            InputVerificationCode(
                verifyCode = uiState.verifyCode,
                updateVerifyCode = { signInViewModel.updateVerifyCode(it) }
            )
        }
        Button(
            onClick = {
                appState.navController.navigate(SIGNIN_PASSWORD_ROUTE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape,
            // TODO 로직수정
            colors = ButtonDefaults.buttonColors(if (uiState.verifyCode.length == 6) Color.Black else Gray300)
        ) {
            Text(
                text = "인증 확인",
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
                    RetryContainer()
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
                }),
            )

        }
    }
}

@Composable
private fun BoxScope.RetryContainer() {
    Row(
        modifier = Modifier.Companion.align(Alignment.CenterEnd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "3:00", color = Error_Color, fontSize = 14.sp)
        WidthSpacer(width = 15.dp)
        Box(modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Blue100)
            .clickable {}) {
            Text(
                text = "재요청",
                modifier = Modifier.padding(14.dp, 7.dp),
                style = Body2M,
                color = Primary
            )
        }
    }
}

