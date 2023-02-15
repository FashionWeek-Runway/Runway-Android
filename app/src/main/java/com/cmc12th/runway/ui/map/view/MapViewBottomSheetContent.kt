package com.cmc12th.runway.ui.map.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.theme.Body1M
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.utils.Constants

@Composable
fun MapViewBottomSheetContent(screenHeight: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(screenHeight - 170.dp)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = Constants.BOTTOM_NAVIGATION_HEIGHT
            )
    ) {
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .height(3.dp)
                .width(36.dp)
                .background(Gray200)
                .align(Alignment.CenterHorizontally)
        )
        HeightSpacer(height = 10.dp)
        Text(text = "[성수동] 둘러보기", style = Body1M, color = Color.Black)

        Column {
            Text(text = "보텀쉿테스트")
            Text(text = "보텀쉿테스트")
            Text(text = "보텀쉿테스트")
            Text(text = "보텀쉿테스트")
        }
    }
}
