package com.cmc12th.runway.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.SplashColor
import com.cmc12th.runway.ui.theme.SplashColor2
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(appState: ApplicationState) {

    val splashViewModel: SplashViewModel = hiltViewModel()

    appState.systmeUiController.setSystemBarsColor(Color.Transparent)
    appState.systmeUiController.setNavigationBarColor(SplashColor2)

    LaunchedEffect(key1 = Unit) {
        delay(1000L)
        splashViewModel.loginCheck(
            navigateToMain = {
                appState.systmeUiController.setStatusBarColor(Color.White)
                appState.systmeUiController.setNavigationBarColor(Color.White)
                appState.navController.navigate(MAIN_GRAPH) {
                    popUpTo(SPLASH_ROUTE) {
                        this.inclusive = true
                    }
                }
            },
            navigateToLogin = {
                appState.navController.navigate(LOGIN_GRAPH) {
                    popUpTo(SPLASH_ROUTE) {
                        this.inclusive = true
                    }
                }
            }
        )
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo_moving))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(SplashColor)
    ) {

        Image(
            painter = painterResource(id = R.mipmap.img_splash_street),
            contentDescription = "IMG_SPLASH_STREET",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 72.dp)
                .fillMaxWidth(0.65f),
            contentScale = ContentScale.Crop
        )
    }

}
