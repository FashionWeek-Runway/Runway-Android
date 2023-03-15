package com.cmc12th.runway.ui.login.view

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.LoginViewModel
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_PROFILE_IMAGE_ROUTE

@Composable
fun LoginBaseScreen(
    appState: ApplicationState,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    val navigateToLogin = {
        appState.navigate(LOGIN_ID_PW_ROUTE)
    }
    val navigateToSignIn = {
        appState.navigate(SIGNIN_GRAPH)
    }
    val navigateToProfileImage: (profileImage: String, kakaoId: String) -> Unit =
        { profileImage, kakaoId ->
            appState.navigate("$SIGNIN_PROFILE_IMAGE_ROUTE?profileImage=$profileImage&kakaoId=$kakaoId")
        }
    val navigateToMain = {
        appState.navController.navigate(MAIN_GRAPH) {
            popUpTo(LOGIN_GRAPH) {
                this.inclusive = true
            }
        }

    }

    DisposableEffect(key1 = Unit) {
        appState.systmeUiController.setSystemBarsColor(Color.Transparent)
        appState.systmeUiController.setNavigationBarColor(SplashColor2)
        onDispose {
            appState.systmeUiController.setStatusBarColor(Color.White)
            appState.systmeUiController.setNavigationBarColor(Color.White)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp, 18.dp),
                painter = painterResource(id = R.mipmap.img_logo_text_point),
                contentDescription = "IMG_LOGO"
            )
            Text(
                text = "내 손 안에 간편한\n패션 쇼핑 지도",
                style = Body1B,
                color = White,
                textAlign = TextAlign.Center
            )
        }

        Box(modifier = Modifier.weight(1f)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .fillMaxHeight(0.5f)
                    .background(SplashColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.5f)
                    .background(SplashColor2)
            )
            Image(
                painter = painterResource(id = R.mipmap.img_splash_street),
                contentDescription = "IMG_SPLASH_STREET",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                contentScale = ContentScale.FillWidth
            )
        }

        /** 아래 가입 부분 */
        BottomLoginButtons(
            kakaoLogin = {
                loginViewModel.getKakaoToken(
                    context = it,
                    alreadyRegistered = {
                        navigateToMain()
                    },
                    notRegistered = { profiletImage, kakaoId ->
                        navigateToProfileImage(profiletImage, kakaoId)
                    },
                    onError = {
                        appState.showSnackbar(it.message)
                    }
                )
            },
            navigateToSignIn = navigateToSignIn, navigateToLogin = navigateToLogin
        )
    }
}


@Composable
private fun BottomLoginButtons(
    navigateToSignIn: () -> Unit,
    navigateToLogin: () -> Unit,
    kakaoLogin: (Context) -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    Column(
        modifier = Modifier.Companion
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
                    .clickable { navigateToLogin() },
                painter = painterResource(id = R.drawable.img_dial_btn),
                contentDescription = "IMG_DIAL_BTN"
            )
        }
    }
}