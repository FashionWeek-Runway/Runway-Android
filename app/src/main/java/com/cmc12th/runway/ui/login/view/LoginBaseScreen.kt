package com.cmc12th.runway.ui.login.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.LoginViewModel
import com.cmc12th.runway.ui.theme.HeadLine1
import com.cmc12th.runway.utils.Constants
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


    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding())
    {
        /** 배경 */
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
        )

        /** 왼쪽 위 런웨이 설명 */
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 150.dp, start = 40.dp)
        ) {
            Text(
                text = "런웨이 설명",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.size(66.dp),
                painter = painterResource(id = R.drawable.img_splash_logo),
                contentDescription = "IMG_SPLASH_LOGO"
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
            fontSize = 14.sp,
            style = HeadLine1
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