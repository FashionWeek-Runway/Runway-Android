package com.cmc12th.runway.ui.mypage.view

import androidx.activity.compose.BackHandler
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
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import com.cmc12th.runway.ui.signin.view.ProfileBox
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.EDIT_PROFILE_IMAGE_ROUTE
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun EditProfileCompleteScreen(
    appState: ApplicationState,
    mypageViewModel: MypageViewModel
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

    val uiState by mypageViewModel.complteUiState.collectAsStateWithLifecycle()

    BackHandler {
        appState.navController.navigate(Constants.MAIN_GRAPH) {
            popUpTo(EDIT_PROFILE_IMAGE_ROUTE) {
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
            Text(
                text = "닉네임 변경 완료!", style = HeadLine2, color = Color.White
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
                appState.navController.navigate(Constants.MAIN_GRAPH) {
                    popUpTo(EDIT_PROFILE_IMAGE_ROUTE) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text(
                text = "확인",
                modifier = Modifier.padding(0.dp, 5.dp),
                style = Button1,
                color = Primary
            )

        }
    }

}

