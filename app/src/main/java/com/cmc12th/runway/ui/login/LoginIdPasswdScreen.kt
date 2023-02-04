package com.cmc12th.runway.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.HeadLine1

@Composable
fun LoginIdPasswdScreen() {

    val phoneTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    val passWdTextFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        /** 상단 뒤로가기 */
        BackIcon()

        /** 로그인 본문 */
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
        ) {
            Text(text = "로그인", style = HeadLine1)
            HeightSpacer(60.dp)
            CustomTextField(
                trailingIcon = {
                    if (phoneTextFieldValue.value.text.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_disable_pw),
                            contentDescription = "Icon Close",
                            modifier = Modifier.clickable {
                                phoneTextFieldValue.value = TextFieldValue()
                            },
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 16.sp,
                value = phoneTextFieldValue.value,
                placeholderText = "전화번호 입력",
                onvalueChanged = { phoneTextFieldValue.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                }),
            )
            // Text(text = "error Message", color = Color.Red)
            HeightSpacer(30.dp)
            CustomTextField(
                trailingIcon = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(1.dp, Gray300),
                fontSize = 16.sp,
                value = passWdTextFieldValue.value,
                placeholderText = "전화번호 입력",
                onvalueChanged = { passWdTextFieldValue.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                }),
            )
            // Text(text = "error Message", color = Color.Red)
            HeightSpacer(30.dp)
            Text(text = "비밀번호 찾기", modifier = Modifier.align(Alignment.End))
        }

        /** 하단 로그인, 회원가입 */
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color.Black)
            )
            {
                Text(text = "로그인", color = Color.White, fontSize = 16.sp)
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.Black),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(text = "회원가입", color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current

        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Preview(backgroundColor = 1)
@Composable
fun LoginIdPasswdScreenPreview() {
    LoginIdPasswdScreen()
}
