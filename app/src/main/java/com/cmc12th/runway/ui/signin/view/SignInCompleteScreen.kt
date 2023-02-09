@file:OptIn(ExperimentalAnimationApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SignInCompleteScreen() {

    val onEnterScreen = remember {
        mutableStateOf(false)
    }

    val scale = remember {
        mutableStateOf(0.6f)
    }

    LaunchedEffect(key1 = Unit) {
        delay(200)
        scale.value = 1.2f
        delay(100)
        scale.value = 0.9f
        delay(100)
        scale.value = 1f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "회원가입 완료!", style = HeadLine2, color = Color.White)
            Text(
                text = "런웨이를 가입한 걸 축하애요!\n이제 내 취향에 맞는 쇼룸을 찾아볼까요?",
                color = Gray500,
                style = Body1,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = scale.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale.value)
                        .padding(start = 50.dp, end = 50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                ) {
                    Column() {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(174.dp)
                                .background(Point)
                        ) {
                            Image(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxHeight()
                                    .aspectRatio(1f),
                                painter = painterResource(id = R.drawable.img_dummy),
                                contentDescription = "PROFILE_IMAGE"
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp, 15.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 3.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.img_congratuation),
                                contentDescription = "IMG_CONGRATUATION"
                            )

                            WidthSpacerLine()
                            Text(text = "NAME", color = Primary, style = Body1)
                            Text(text = "닉네임이열자까지래요", color = Primary, style = HeadLine2)
                            WidthSpacerLine()
                            Text(text = "STYLE", color = Primary, style = Body1)
                            Row(
                                modifier = Modifier.align(Alignment.End),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "미니멀",
                                    modifier = Modifier
                                        .background(Primary)
                                        .padding(8.dp, 4.dp),
                                    color = Color.White
                                )
                                Text(
                                    text = "캐주얼",
                                    modifier = Modifier
                                        .background(Primary)
                                        .padding(8.dp, 4.dp),
                                    color = Color.White
                                )
                                Text(
                                    text = "+2",
                                    modifier = Modifier
                                        .background(Primary)
                                        .padding(8.dp, 4.dp),
                                    color = Color.White
                                )
                            }
                            WidthSpacerLine()
                            Image(
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.ic_logo_barcode),
                                contentDescription = "IC_LOGO_BARCOCE",
                                modifier = Modifier
                                    .fillMaxWidth(0.4f)
                                    .wrapContentHeight()
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }

        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp),
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
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Primary)
    )
}

@Composable
@Preview
fun SignInCompleteScreenPrview() {
    SignInCompleteScreen()
}