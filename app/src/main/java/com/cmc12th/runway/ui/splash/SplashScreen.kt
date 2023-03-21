package com.cmc12th.runway.ui.splash

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.LoginViewModel
import com.cmc12th.runway.ui.splash.component.BottomLoginButtons
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.SplashColor
import com.cmc12th.runway.ui.theme.SplashColor2
import com.cmc12th.runway.ui.theme.White
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(appState: ApplicationState) {

    val splashViewModel: SplashViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()

    var toLoginState by remember {
        mutableStateOf(false)
    }

    appState.systmeUiController.setSystemBarsColor(Color.Transparent)
    appState.systmeUiController.setNavigationBarColor(SplashColor2)

    val navigateToMain = {
        appState.systmeUiController.setStatusBarColor(Color.White)
        appState.systmeUiController.setNavigationBarColor(Color.White)
        appState.navController.navigate(MAIN_GRAPH) {
            popUpTo(SPLASH_ROUTE) {
                inclusive = true
                saveState = true
            }
        }
    }

    val navigateToProfileImage: (profileImage: String, kakaoId: String) -> Unit =
        { profileImage, kakaoId ->
            appState.navigate("${Constants.SIGNIN_PROFILE_IMAGE_ROUTE}?profileImage=$profileImage&kakaoId=$kakaoId")
        }

    val navigateToSignIn = {
        appState.navigate(SIGNIN_GRAPH)
    }
    val navigateToLogin = {
        toLoginState = true
    }
    val navigateToLoginIdPasswd = {
        appState.systmeUiController.setStatusBarColor(Color.White)
        appState.systmeUiController.setNavigationBarColor(Color.White)
        appState.navigate(LOGIN_ID_PW_ROUTE)
    }

    val kakaoLogin: (Context) -> Unit = {
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
    }

    LaunchedEffect(key1 = Unit) {
        delay(1500L)
        splashViewModel.loginCheck(
            navigateToMain = navigateToMain,
            navigateToLogin = navigateToLogin
        )
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo_moving))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashColor)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = !toLoginState) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .padding(top = 72.dp)
                    .fillMaxWidth(0.65f),
                contentScale = ContentScale.Crop
            )
        }
        AnimatedVisibility(visible = toLoginState) {
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
        }

        if (!toLoginState) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier =
            splashWeightByLoading(!toLoginState)
        ) {
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


        AnimatedVisibility(
            visible = toLoginState,
            modifier = Modifier
                .fillMaxWidth()
                .background(SplashColor2)
        ) {
            BottomLoginButtons(
                kakaoLogin = kakaoLogin,
                navigateToSignIn = navigateToSignIn,
                navigateToLoginIdPasswd = navigateToLoginIdPasswd
            )
        }
    }
}


private fun ColumnScope.splashWeightByLoading(isLoading: Boolean): Modifier =
    if (isLoading) Modifier else Modifier.weight(1f)
