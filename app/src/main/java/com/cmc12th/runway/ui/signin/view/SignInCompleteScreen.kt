package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.theme.*

@Composable
fun SignInCompleteScreen() {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "회원가입 완료!", style = HeadLine2, color = Color.White)
            Text(text = "런웨이를 가입한 걸 축하애요!\n이제 내 취향에 맞는 쇼룸을 찾아볼까요?",
                color = Gray500,
                style = Body1,
                textAlign = TextAlign.Center)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp, start = 50.dp, end = 50.dp, bottom = 50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent)
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent)
            ) {
                Box {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .fillMaxHeight(0.3f)
                        .background(Point)) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                                .aspectRatio(1f),
                            painter = painterResource(id = R.drawable.img_dummy),
                            contentDescription = "PROFILE_IMAGE")
                    }

                    Column(modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .padding(20.dp, 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                    ) {
                        Text(text = "CONGRATUDATION", color = Primary)
                        WidthSpacerLine()
                        Text(text = "NAME")
                        Text(text = "나패피", fontSize = 18.sp)
                        WidthSpacerLine()
                        Text(text = "STYLE")
                        Row(modifier = Modifier.align(Alignment.End),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(text = "미니멀",
                                modifier = Modifier.background(Primary),
                                color = Color.White)
                            Text(text = "미니멀",
                                modifier = Modifier.background(Primary),
                                color = Color.White)
                        }
                        WidthSpacerLine()
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo_barcode),
                            contentDescription = "IC_LOGO_BARCOCE",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Point),
            onClick = {
                // TODO
            }
        ) {
            Text(
                text = "홈으로",
                modifier = Modifier.padding(0.dp, 5.dp),
                style = Button1,
                color = Primary
            )
        }

    }
}

@Composable
private fun WidthSpacerLine() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Primary))
}

@Composable
@Preview
fun SignInCompleteScreenPrview() {
    SignInCompleteScreen()
}