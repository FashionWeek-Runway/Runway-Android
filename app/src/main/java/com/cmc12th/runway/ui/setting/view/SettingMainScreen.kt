package com.cmc12th.runway.ui.setting

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.RunwayHorizontalDialog
import com.cmc12th.runway.ui.components.RunwaySwitch
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Body1M
import com.cmc12th.runway.ui.theme.Body2B
import com.cmc12th.runway.ui.theme.Gray600
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.LOCATION_USE_TERMS
import com.cmc12th.runway.utils.Constants.MARKETING_INFO_TERMS
import com.cmc12th.runway.utils.Constants.PERSONAL_INFO_USE_TERMS
import com.cmc12th.runway.utils.Constants.SERVICE_TERMS
import com.cmc12th.runway.utils.Constants.SETTING_PERSONAL_INFO_MANAGEMENT_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE


data class MypageItemWrapper(
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun SettingMainScreen(
    appState: ApplicationState,
    viewModel: SettingViewModel,
) {

    val account = listOf(MypageItemWrapper("개인 정보 관리", onClick = {
        appState.navigate(SETTING_PERSONAL_INFO_MANAGEMENT_ROUTE)
    }))
    val inquiry = listOf(MypageItemWrapper("쇼룸 추가 요청", onClick = {
        appState.navigate("${WEB_VIEW_ROUTE}?title=${""}&url=${"https://docs.google.com/forms/d/e/1FAIpQLScmcpp4nMMziU6t8xgsct18IKeYXy2KTjNDIdRDILKv4bazkA/viewform"}")
    }))

    val context = LocalContext.current
    val policies = listOf(
        MypageItemWrapper("이용약관", onClick = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SERVICE_TERMS)))
        }),
        MypageItemWrapper("개인정보 처리 방침", onClick = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PERSONAL_INFO_USE_TERMS)))
        }),
        MypageItemWrapper("위치정보 이용 약관", onClick = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LOCATION_USE_TERMS)))
        }),
        MypageItemWrapper("마케팅 정보 수신 동의 약관", onClick = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKETING_INFO_TERMS)))
        }),
    )

    val logout: () -> Unit = {
        viewModel.logout {
            appState.navController.navigate(Constants.LOGIN_GRAPH) {
                popUpTo(Constants.MAIN_GRAPH) {
                    inclusive = true
                }
            }
        }
    }

    val alarmState = remember {
        mutableStateOf(false)
    }

    val logoutDialog = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (logoutDialog.value) {
            RunwayHorizontalDialog(
                onDismissRequest = { logoutDialog.value = false },
                title = "로그아웃",
                descrption = "RUNWAY의 힙한 매장을 볼 수 없어요.\n정말 로그아웃 하시겠어요?",
                positiveButton = DialogButtonContent(
                    title = "로그아웃",
                    onClick = {
                        logout()
                    }
                ),
                negativeButton = DialogButtonContent(
                    title = "취소",
                    onClick = {
                        logoutDialog.value = false
                    }
                )
            )
        }

        Row(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "설정", style = Body1B, color = Color.Black)
        }

        /** Account */
        BaseSettingWrapper(title = "계정", items = account)

        /** 알람 */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 12.dp),
        ) {
            Text(
                text = "알림",
                modifier = Modifier.padding(vertical = 12.dp),
                style = Body2B,
                color = Gray600
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "푸시 알림",
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .weight(1f)
                )
                RunwaySwitch(
                    isSelected = alarmState.value,
                    updateSelected = {
                        alarmState.value = !alarmState.value
                    }
                )
            }
        }

        /** 문의 */
        BaseSettingWrapper(title = "문의", items = inquiry)

        /** 약관 및 정책 */
        BaseSettingWrapper(title = "약관 및 정책", items = policies)

        /** 버전 정보 */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "버전 정보",
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .weight(1f)
                )
                Text(text = "[버전]")
            }
        }

        /** 로그 아웃 */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 12.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    logoutDialog.value = true
                }) {
                Text(
                    text = "로그아웃",
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun BaseSettingWrapper(title: String, items: List<MypageItemWrapper>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 12.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(vertical = 12.dp),
            style = Body2B,
            color = Gray600
        )
        items.forEach {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    it.onClick()
                }) {
                Text(
                    text = it.title,
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
        }

    }
}
