package com.cmc12th.runway.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(appState: ApplicationState) {

    val splashViewModel: SplashViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit) {
        delay(200L)
        splashViewModel.loginCheck(
            navigateToMain = {
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

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            painter = painterResource(id = R.drawable.img_splash_background),
            contentDescription = "SPLAH_BACKGROUND",
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(94.dp, 100.dp),
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "SPLAH_LOGO"
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            text = "RUNWAY",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


