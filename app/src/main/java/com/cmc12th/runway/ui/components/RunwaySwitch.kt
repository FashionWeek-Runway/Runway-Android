package com.cmc12th.runway.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Primary


@Composable
fun RunwaySwitch(
    isSelected: Boolean,
    updateSelected: () -> Unit,
    width: Dp = 40.dp,
    height: Dp = 24.dp,
    checkedTrackColor: Color = Primary,
    uncheckedTrackColor: Color = Gray300,
    gapBetweenThumbAndTrackEdge: Dp = 2.dp,
) {

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    val animateColor =
        animateColorAsState(targetValue = if (isSelected) checkedTrackColor else uncheckedTrackColor)

    val animatePosition = animateFloatAsState(
        targetValue = if (isSelected)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        updateSelected()
                    }
                )
            }
    ) {
        // Track
        drawRoundRect(
            color = animateColor.value,
            cornerRadius = CornerRadius(x = 20.dp.toPx(), y = 20.dp.toPx()),
        )

        // Thumb
        drawCircle(
            color = Color.White,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}