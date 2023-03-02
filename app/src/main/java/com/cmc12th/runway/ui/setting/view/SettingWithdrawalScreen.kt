package com.cmc12th.runway.ui.setting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.RunwayCheckBox
import com.cmc12th.runway.ui.theme.*

@Preview
@Composable
fun SettingWithdrawalScreen() {

    val withDrawalCheckbox = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                // appState.popBackStack()
            }
            Text(text = "개인 정보 관리", style = Body1B, color = Color.Black)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "[닉네임]님.. 정말 탈퇴하시겠어요..?", style = HeadLine4, color = Black)
            Text(text = "너무 아쉽지만..\n떠나기 전에 아래 내용을 확인해주세요.", style = Body1, color = Black)
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Gray50)
            .padding(14.dp, 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {

                    Text(text = "15일 이후 처음부터 다시 가입해야해요", style = Body2B, color = Black)
                }
                Text(text = "탈퇴 회원 정보는 15일간 임시 보관 후 완벽히 삭제됩니다. 탈퇴하시면 회원가입부터 다시 해야해요.",
                    style = Body2,
                    color = Gray800)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {

                    Text(text = "작성한 후기가 사라져요", style = Body2B, color = Black)
                }
                Text(text = "회원님이 작성한 후기들이 영구적으로 삭제됩니다. \n삭제된 정보는 다시 복구할 수 없어요.",
                    style = Body2,
                    color = Gray800)
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                withDrawalCheckbox.value = !withDrawalCheckbox.value
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            RunwayCheckBox(checkState = withDrawalCheckbox.value) {
                withDrawalCheckbox.value = !withDrawalCheckbox.value
            }
            Text(text = "위의 내용을 모두 확인하였으며, 탈퇴하겠습니다.", style = Body2, color = Gray900)
        }
    }
}