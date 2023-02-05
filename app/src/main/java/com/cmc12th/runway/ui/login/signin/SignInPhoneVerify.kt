@file:OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

package com.cmc12th.runway.ui.login.signin

import android.view.ViewTreeObserver
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.theme.Error_Color
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.HeadLine3
import kotlinx.coroutines.launch

@Composable
fun SignInPhoneVerifyScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            Row {
                Text(text = "인증번호", style = HeadLine3)
                Text(text = "를 입력해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
            HeightSpacer(height = 40.dp)
            /** 인증번호 입력 */
            InputVerificationCode()
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Gray300)
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
private fun InputVerificationCode() {
    // TODO ViewModel 추출
    val verificationCode = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        Box(Modifier.weight(2f)) {
            CustomTextField(
                trailingIcon = {
                    Text(text = "3:00", color = Error_Color)
                },
                modifier = Modifier,
                fontSize = 16.sp,
                focusRequest = focusRequester,
                value = verificationCode.value,
                placeholderText = "숫자 6자리 입력",
                onvalueChanged = { if (it.text.length <= 6) verificationCode.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                }),
            )
        }
        WidthSpacer(15.dp)
        Button(
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier
                .weight(1f)
                .border(BorderStroke(1.dp, Color.Black)),
            onClick = { /*TODO*/ }) {
            Text(text = "재요청", fontSize = 16.sp, color = Color.Black)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SignInPhoneVerifyScreenPreview() {
    SignInPhoneVerifyScreen()
}
