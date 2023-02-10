package com.cmc12th.runway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun WidthSpacerLine(height: Dp, color: Color) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
    )
}
