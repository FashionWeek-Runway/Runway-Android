@file:OptIn(ExperimentalAnimationApi::class)

package com.cmc12th.runway.ui.signin.view

import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.KeyboardStatus
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SignInCompleteScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    /** Bouncing 애니메이션 적용 */
    val scale = remember {
        mutableStateOf(0.7f)
    }
    val animatedScale = animateFloatAsState(
        targetValue = scale.value,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(key1 = Unit) {
        delay(200)
        scale.value = 1.15f
        delay(200)
        scale.value = 0.9f
        delay(150)
        scale.value = 1.1f
        delay(70)
        scale.value = 0.95f
        delay(50)
        scale.value = 1f
    }

    /** StatusBar Color 변경 */
    val systemUiController = rememberSystemUiController()
    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Black
        )
        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.White
            )
        }
    }

    val uiState by signInViewModel.complteUiState.collectAsStateWithLifecycle()

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
            ProfileBox(
                animatedScale = animatedScale,
                nickname = uiState.nickName,
                categoryTags = uiState.categoryTags,
                image = uiState.profileImage
            )
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
private fun ProfileBox(
    animatedScale: State<Float>,
    nickname: Nickname,
    categoryTags: List<CategoryTag>,
    image: Uri?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale.value)
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
                if (image == null) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "PROFILE_IMAGE"
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop,
                        model = ImageRequest.Builder(LocalContext.current).data(image).build(),
                        contentDescription = "PROFILE_IMAGE"
                    )
                }

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
                WidthSpacerLine(1.dp, Primary)
                Text(text = "NAME", color = Primary, style = Body1)
                Text(text = nickname.text, color = Primary, style = HeadLine2)
                WidthSpacerLine(1.dp, Primary)
                Text(text = "STYLE", color = Primary, style = Body1)
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    categoryTags.take(2).map {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .background(Primary)
                                .padding(8.dp, 4.dp),
                            color = Color.White
                        )
                    }

                    if (categoryTags.size > 2) {
                        Text(
                            text = "+${categoryTags.size - 2}",
                            modifier = Modifier
                                .background(Primary)
                                .padding(8.dp, 4.dp),
                            color = Color.White
                        )
                    }

                }
                WidthSpacerLine(1.dp, Primary)
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

