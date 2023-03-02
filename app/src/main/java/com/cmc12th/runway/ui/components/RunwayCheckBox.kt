package com.cmc12th.runway.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Point
import com.cmc12th.runway.ui.theme.Primary


@Composable
fun RunwayCheckBox(
    checkState: Boolean,
    onChecked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(if (checkState) 0.dp else 1.dp, Gray300))
            .background(if (checkState) Primary else Color.White)
            .padding(3.dp)
            .clickable {
                onChecked()
            }
    ) {
        AnimatedVisibility(
            visible = checkState, modifier = Modifier
                .align(Alignment.Center)
        ) {
            if (checkState) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "IC_CHECK",
                    tint = Point
                )
            }
        }
    }
}
