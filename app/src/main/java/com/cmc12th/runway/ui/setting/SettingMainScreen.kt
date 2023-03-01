package com.cmc12th.runway.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.Body1B

@Composable
fun SettingMainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
    }
}
