package com.cmc12th.runway.ui.login.signin.agreement

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.theme.Body1
import com.cmc12th.runway.ui.theme.HeadLine3
import com.cmc12th.runway.ui.theme.HeadLine4

@Composable
fun AgreementDetailScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Row {
                BackIcon()
                WidthSpacer(width = 20.dp)
                Text(text = "이용약관 동의(필수)", style = Body1)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp, 10.dp)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        ) {
            /** 약관동의 텍스트 */
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .weight(1f)
            ) {
                Text(text = "타이틀", style = HeadLine4)
                HeightSpacer(height = 20.dp)
                Text(text = "내용", style = Body1)
            }
        }
    }
}

@Preview
@Composable
fun AgreementDetailScreenPreview() {
    AgreementDetailScreen()
}