package com.cmc12th.runway.ui.setting.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.RunwayHorizontalDialog
import com.cmc12th.runway.ui.components.RunwaySwitch
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.setting.SettingViewModel
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SETTING_VERIFY_PASSWORD_ROTUE
import com.cmc12th.runway.utils.Constants.SETTING_WITHDRAWAL_ROUTE

//social 값이 true = FRAME setting 03 , false = FRAME setting 02 kakao,apple boolean 값으로 화면에 보여주면 됩니다!
@Composable
fun SettingPersonalInfoManagementScreen(appState: ApplicationState, viewModel: SettingViewModel) {

    val uiState = viewModel.personalInfoUiState.collectAsStateWithLifecycle()

    val unlinkeKakaoState = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getPersonalInfo()
        viewModel.getNickname()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (unlinkeKakaoState.value) {
            RunwayHorizontalDialog(
                onDismissRequest = { unlinkeKakaoState.value = false },
                title = "계정 연결 해제",
                descrption = "카카오 계정 연결을 헤제하시겠습니까?",
                positiveButton = DialogButtonContent(
                    title = "연결 해제",
                    onClick = {
                        viewModel.unLinkToKakao(
                            onError = {
                                appState.showSnackbar(it.message)
                            }
                        )
                    }
                ),
                negativeButton = DialogButtonContent(title = "취소",
                    onClick = {
                        unlinkeKakaoState.value = false
                    })
            )
        }

        Row(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "개인 정보 관리", style = Body1B, color = Color.Black)
        }

        if (!uiState.value.personalInfo.social) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 12.dp),
            ) {
                Text(
                    text = "로그인 정보",
                    modifier = Modifier.padding(vertical = 12.dp),
                    style = Body1B,
                    color = Gray600
                )
                Row(
                    modifier = Modifier
                        .padding(0.dp, 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "전화번호", style = Body1M, color = Black)
                    val phoneNumber = uiState.value.personalInfo.phone
                    Text(
                        text = if (phoneNumber == "-") "-" else
                            "${phoneNumber.slice(0..2)}-****-${phoneNumber.slice(7..10)}",
                        style = Body1M,
                        color = Black
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp, 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "비밀번호 변경", style = Body1M, color = Black)
                    Button(
                        onClick = {
                            appState.navigate(SETTING_VERIFY_PASSWORD_ROTUE)
                        },
                        colors = ButtonDefaults.buttonColors(Blue100),
                        contentPadding = PaddingValues(14.dp, 8.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(text = "변경", style = Body2M, color = Primary)
                    }
                }
            }
        }

        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "SNS 연결",
                    style = Body1B,
                    color = Gray600
                )
                Text(text = "연결된 SNS 계정으로 로그인할 수 있습니다.", style = Body2, color = Gray800)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.img_kakao_btn),
                    contentDescription = "IMG_KAKAO_BTN",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "카카오 로그인 연결",
                    style = Body1,
                    color = Black,
                    modifier = Modifier.weight(1f)
                )
                if (uiState.value.personalInfo.social) {
                    Text(text = "연결 완료", style = Body2M, color = Gray600)
                } else {
                    RunwaySwitch(isSelected = uiState.value.personalInfo.kakao,
                        updateSelected = {
                            if (uiState.value.personalInfo.kakao) {
                                unlinkeKakaoState.value = true
                            } else {
                                viewModel.getKakaoToken(
                                    onSuccess = {
                                        appState.showSnackbar(message = "계정이 연결되었습니다.")
                                        viewModel.updateKakaoLinkState(true)
                                    },
                                    onError = {
                                        appState.showSnackbar(it.message)
                                    },
                                    context = context
                                )
                            }
                        })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "회원 탈퇴", style = Body2, color = Gray800, modifier = Modifier.clickable {
                appState.navigate(SETTING_WITHDRAWAL_ROUTE)
            })
        }

    }

}

