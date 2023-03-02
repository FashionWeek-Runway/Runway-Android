package com.cmc12th.runway.ui.setting

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.RunwaySwitch
import com.cmc12th.runway.ui.theme.*


data class SettingItem(
    val title: String,
    val onClick: () -> Unit,
)

@Preview
@Composable
fun SettingMainScreen() {

    val account = listOf(SettingItem("개인 정보 관리", onClick = {}))
    val inquiry = listOf(SettingItem("쇼룸 추가 요청", onClick = {}))
    val policies = listOf(
        SettingItem("이용약관", onClick = {}),
        SettingItem("개인정보 처리 방침", onClick = {}),
        SettingItem("위치정보 이용 약관", onClick = {}),
        SettingItem("마케팅 정보 수신 동의 약관", onClick = {}),
    )

    val alaramState = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                // appState.popBackStack()
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
            Text(text = "알림",
                modifier = Modifier.padding(vertical = 12.dp),
                style = Body2B,
                color = Gray600)
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "푸시 알림",
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .weight(1f))
                RunwaySwitch(
                    isSelected = alaramState.value,
                    updateSelected = {
                        alaramState.value = !alaramState.value
                    }
                )
            }
        }

        /** 문의 */
        BaseSettingWrapper(title = "문의", items = inquiry)

        /** 약관 및 정책 */
        BaseSettingWrapper(title = "약관 및 정책", items = policies)

        /** 버전 정보 */
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "버전 정보",
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .weight(1f))
                Text(text = "[버전]")
            }
        }

        /** 로그 아웃 */
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 12.dp)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                }) {
                Text(text = "로그아웃",
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
private fun BaseSettingWrapper(title: String, items: List<SettingItem>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp, 12.dp)) {
        Text(text = title,
            modifier = Modifier.padding(vertical = 12.dp),
            style = Body2B,
            color = Gray600)
        items.forEach {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    it.onClick()
                }) {
                Text(text = it.title,
                    style = Body1M,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
        }

    }
}
