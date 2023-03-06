package com.cmc12th.runway.ui.login.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.LoginViewModel
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1
import com.cmc12th.runway.ui.theme.White
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

        appState.systmeUiController.setStatusBarColor(Color.Black)
        appState.systmeUiController.setNavigationBarColor(Color.Black)

        onDispose {
            appState.systmeUiController.setStatusBarColor(Color.White)
            appState.systmeUiController.setNavigationBarColor(Color.White)

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    )
    {

        Column(verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(54.dp),
                painter = painterResource(id = R.mipmap.img_logo_point),
                contentDescription = "IMG_LOGO"
            )
            Image(
                modifier = Modifier.size(100.dp, 18.dp),
                painter = painterResource(id = R.mipmap.img_logo_text_point),
                contentDescription = "IMG_LOGO"
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
private fun BoxScope.BottomLoginButtons(
    navigateToSignIn: () -> Unit,
    navigateToLogin: () -> Unit,
    kakaoLogin: (Context) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.Companion
            .align(Alignment.BottomCenter)
            .fillMaxHeight(0.2f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable { navigateToSignIn() },
            text = "간편하게 가입하기",
            style = Body1,
            color = White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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