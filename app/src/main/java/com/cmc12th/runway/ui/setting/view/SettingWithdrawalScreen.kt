package com.cmc12th.runway.ui.setting.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.RunwayCheckBox
import com.cmc12th.runway.ui.components.RunwayVerticalDialog
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.setting.SettingViewModel
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE

@Composable
fun SettingWithdrawalScreen(
    appState: ApplicationState,
    viewModel: SettingViewModel,
) {

    val uiState by viewModel.personalInfoUiState.collectAsStateWithLifecycle()

    val withDrawalCheckbox = remember {
        mutableStateOf(false)
    }

    val withDrawalDialog = remember {
        mutableStateOf(false)
    }
    val navigateToOnboard: () -> Unit = {
        appState.navController.navigate(Constants.LOGIN_GRAPH) {
            popUpTo(Constants.MAIN_GRAPH) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        if (withDrawalDialog.value) {
            RunwayVerticalDialog(
                properties = DialogProperties(dismissOnClickOutside = false),
                onDismissRequest = {
                    navigateToOnboard()
                },
                title = "회원 탈퇴 완료",
                descrption = "15일 이전에 돌아오시면 로그인이\n가능합니다. RUNWAY가 보고싶다면\n언제든지 돌아오세요!",
                positiveButton = DialogButtonContent(
                    title = "확인",
                    onClick = {
                        navigateToOnboard()
                    })
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "개인 정보 관리", style = Body1B, color = Color.Black)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "${uiState.nickname}님 정말 탈퇴하시겠어요..?", style = HeadLine4, color = Black)
            Text(text = "너무 아쉽지만..\n떠나기 전에 아래 내용을 확인해주세요.", style = Body1, color = Black)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Gray50)
                .padding(14.dp, 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_emoticon_sadface),
                        contentDescription = "IMG_EMOTICON_SADFACE",
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "15일 이후 처음부터 다시 가입해야해요", style = Body2B, color = Black)
                }
                Text(
                    text = "탈퇴 회원 정보는 15일간 임시 보관 후 완벽히 삭제됩니다. 탈퇴하시면 회원가입부터 다시 해야해요.",
                    style = Body2,
                    color = Gray800
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_emoticon_trachcan),
                        contentDescription = "IMG_EMOTICON_TRASHCAN",
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "작성한 후기가 사라져요", style = Body2B, color = Black)
                }

                Text(
                    text = "회원님이 작성한 후기들이 영구적으로 삭제됩니다. \n삭제된 정보는 다시 복구할 수 없어요.",
                    style = Body2,
                    color = Gray800
                )

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    withDrawalCheckbox.value = !withDrawalCheckbox.value
                },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RunwayCheckBox(checkState = withDrawalCheckbox.value) {
                withDrawalCheckbox.value = !withDrawalCheckbox.value
            }
            Text(text = "위의 내용을 모두 확인하였으며, 탈퇴하겠습니다.", style = Body2, color = Gray900)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.withdrawal {
                    appState.navController.navigate(SPLASH_ROUTE) {
                        popUpTo(MAIN_GRAPH) {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = withDrawalCheckbox.value,
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, if (withDrawalCheckbox.value) Black else Gray300),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                disabledContainerColor = White
            ),
        ) {
            Text(
                text = "탈퇴하기",
                modifier = Modifier.padding(0.dp, 5.dp),
                textAlign = TextAlign.Center,
                color = if (withDrawalCheckbox.value) Black else Gray300,
                style = Button1
            )
        }

    }
}