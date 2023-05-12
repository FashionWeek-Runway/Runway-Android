package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.Body1M
import com.cmc12th.runway.ui.theme.HeadLine4

@Composable
fun AgreementDetailScreen(
    appState: ApplicationState,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BackIcon {
                    appState.popBackStack()
                }
                WidthSpacer(width = 20.dp)
                Text(text = "이용약관 동의(필수)", style = Body1M)
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
                Text(text = "내용", style = Body1M)
            }
        }
    }
}

