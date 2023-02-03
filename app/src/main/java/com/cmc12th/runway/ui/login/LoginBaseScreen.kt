package com.cmc12th.runway.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R

@Composable
fun LoginBaseScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        /** 배경 */
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.img_login_background),
            contentDescription = "IMG_LOGIN_BACKGROUND")

        /** 왼쪽 위 런웨이 설명 */
        Column(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(top = 150.dp, start = 40.dp)) {
            Text(text = "런웨이 설명",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White)
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.size(66.dp),
                painter = painterResource(id = R.drawable.img_splash_logo),
                contentDescription = "IMG_SPLASH_LOGO")
        }

        /** 아래 가입 부분 */
        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxHeight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "간편하게 가입하기", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.img_kakao_btn),
                    contentDescription = "IMG_KAKAO_BTN")
                Image(
                    modifier = Modifier.size(60.dp),
                    painter = painterResource(id = R.drawable.img_dial_btn),
                    contentDescription = "IMG_DIAL_BTN")
            }
        }
    }
}

@Preview
@Composable
fun LoginBaseScreenPreview() {
    LoginBaseScreen()
}