package com.cmc12th.runway.ui.map.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.Blue600
import com.cmc12th.runway.ui.theme.Body2

@Composable
fun BoxScope.RefreshIcon(
    visibility: Boolean,
    yOffset: Dp,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .statusBarsPadding()
            .align(Alignment.TopCenter),
        visible = visibility,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .offset(y = yOffset)
                .clip(RoundedCornerShape(37.dp))
                .background(Color.White)
                .clickable {
                    onClick()
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp, 9.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_refresh_18),
                    contentDescription = "IC_BASELINE_REFRESH",
                    modifier = Modifier.size(18.dp),
                    tint = Blue600
                )
                WidthSpacer(width = 4.dp)
                Text(text = "현 지도에서 검색", style = Body2, color = Blue600)
            }
        }
    }
}
