package com.cmc12th.runway.ui.login.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.theme.HeadLine3
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.Gray500

@Composable
fun SignInPasswordScreen(appState: ApplicationState) {
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
            Row {
                Text(text = "비밀번호", style = HeadLine3)
                Text(text = "를 입력해주세요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
            }
            /** 패스워드 입력 */
            HeightSpacer(height = 30.dp)
            InputPassword()

            /** 패스워드 확인 */
            HeightSpacer(height = 30.dp)
            CheckPassword()
        }
    }
}

@Composable
fun CheckPassword() {

    val passwdTextField = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            value = passwdTextField.value,
            placeholderText = "비밀번호 확인",
            onvalueChanged = { passwdTextField.value = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
            }),
        )
        HeightSpacer(height = 10.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row() {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "IC_CHECK"
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "비밀번호 일치",
                    fontSize = 14.sp,
                    color = Gray500
                )
            }

        }
    }
}

@Composable
fun InputPassword() {

    val passwdTextField = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            value = passwdTextField.value,
            placeholderText = "영문, 숫자, 조합 8~16자",
            onvalueChanged = { passwdTextField.value = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = {
            }),
        )
        HeightSpacer(height = 10.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row() {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "IC_CHECK"
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "영문",
                    fontSize = 14.sp,
                    color = Gray500
                )
            }

        }
    }
}

