@file:OptIn(ExperimentalAnimationApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SignInCompleteScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    /** Bouncing ??????????????? ?????? */
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

    /** StatusBar Color ?????? */
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

    BackHandler {
        appState.navController.navigate(MAIN_GRAPH) {
            popUpTo(LOGIN_GRAPH) {
                inclusive = true
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "???????????? ??????!", style = HeadLine2, color = Color.White)
            Text(
                text = "???????????? ????????? ??? ????????????!\n?????? ??? ????????? ?????? ????????? ????????????????",
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
                animatedScale = animatedScale.value,
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
                appState.navController.navigate(MAIN_GRAPH) {
                    popUpTo(LOGIN_GRAPH)
                }
            }
        ) {
            Text(
                text = "?????????",
                modifier = Modifier.padding(0.dp, 5.dp),
                style = Button1,
                color = Primary
            )

        }
    }

}

@Composable
@Preview
fun ProfileBoxPreview() {
    ProfileBox(
        1f, Nickname("?????????"), emptyList(), ProfileImageType.DEFAULT
    )
}

@Composable
fun ProfileBox(
    animatedScale: Float,
    nickname: Nickname,
    categoryTags: List<CategoryTag>,
    image: ProfileImageType,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale)
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
                when (image) {
                    is ProfileImageType.DEFAULT -> {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(id = R.drawable.img_profile_default),
                            contentDescription = "PROFILE_IMAGE"
                        )
                    }
                    else -> {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop,
                            painter = rememberAsyncImagePainter(
                                model = when (image) {
                                    is ProfileImageType.LOCAL -> image.uri
                                    is ProfileImageType.SOCIAL -> image.imgUrl
                                    else -> {}
                                }
                            ),
                            contentDescription = "PROFILE_IMAGE"
                        )
                    }
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
                        .padding(top = 3.dp, bottom = 7.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.mipmap.img_congratuation),
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

