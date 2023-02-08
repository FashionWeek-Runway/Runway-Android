package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.theme.Point

@Composable
fun SignInCompleteScreen() {

    Column(modifier = Modifier.fillMaxSize()) {

        Text(text = "회원가입 완료!")
        Text(text = "런웨이를 가입한 걸 축하애요!")
        Text(text = "이제 내 취향에 맞는 쇼룸을 찾아볼까요?")

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Point),
            onClick = {}
        ) {
            Text(
                text = "홈으로",
                modifier = Modifier.padding(0.dp, 5.dp),
                fontSize = 16.sp
            )
        }

    }
}

@Composable
@Preview
fun SignInCompleteScreenPrview() {
    SignInCompleteScreen()
}