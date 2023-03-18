package com.cmc12th.runway.ui.splash.component

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Point
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.ui.theme.SplashColor2

@Composable
fun BottomLoginButtons(
    navigateToSignIn: () -> Unit,
    navigateToLoginIdPasswd: () -> Unit,
    kakaoLogin: (Context) -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .background(SplashColor2)
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Point)
            ) {
                Text(
                    text = "런웨이 입장하기",
                    style = Body1B,
                    color = Primary,
                    modifier = Modifier.padding(20.dp, 10.dp),
                    textAlign = TextAlign.Center
                )
            }
            Canvas(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(18.dp)
            ) {
                drawPath(path = Path().apply {
                    moveTo((size.width / 2f) - with(density) { 11.5.dp.toPx() }, 0f)
                    lineTo((size.width / 2f) + with(density) { 11.5.dp.toPx() }, 0f)
                    lineTo(size.width / 2f, size.height)
                    close()
                }, color = Point)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clickable { kakaoLogin(context) },
                painter = painterResource(id = R.drawable.img_kakao_btn),
                contentDescription = "IMG_KAKAO_BTN"
            )
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clickable { navigateToLoginIdPasswd() },
                painter = painterResource(id = R.drawable.img_dial_btn),
                contentDescription = "IMG_DIAL_BTN"
            )
        }
    }
}