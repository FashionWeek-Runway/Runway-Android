package com.cmc12th.runway.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer

@Composable
fun LoginIdPasswdScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ) {
        /** 상단 뒤로가기 */
        BackIcon()

        /** 로그인 본문 */
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)) {
            Text(
                text = "로그인",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            HeightSpacer(50.dp)
            BasicTextField(value = "전화번호", onValueChange = {}, modifier = Modifier.fillMaxWidth())
            Text(text = "error Message", color = Color.Red)
            HeightSpacer(30.dp)
            BasicTextField(value = "비밀번호", onValueChange = {}, modifier = Modifier.fillMaxWidth())
            Text(text = "error Message", color = Color.Red)
            HeightSpacer(10.dp)
            Text(text = "비밀번호 찾기", modifier = Modifier.align(Alignment.End))
        }

        /** 하단 로그인, 회원가입 */
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(Color.Black))
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

@Preview(backgroundColor = 1)
@Composable
fun LoginIdPasswdScreenPreview() {
    LoginIdPasswdScreen()
}
