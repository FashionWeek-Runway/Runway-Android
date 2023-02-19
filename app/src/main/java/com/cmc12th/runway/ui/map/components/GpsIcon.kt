package com.cmc12th.runway.ui.map.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R

@Composable
fun BoxScope.GpsIcon(
    visiblitiy: Boolean,
    offsetY: Dp,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.TopEnd),
        visible = !visiblitiy,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        IconButton(
            modifier = Modifier
                .offset(
                    x = (-20).dp,
                    y = offsetY - 50.dp
                )
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_location_24),
                contentDescription = "IC_BASELINE_LOCATION"
            )
        }
    }
}