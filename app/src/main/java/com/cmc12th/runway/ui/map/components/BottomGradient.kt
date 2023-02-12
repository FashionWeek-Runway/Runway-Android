package com.cmc12th.runway.ui.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomGradient() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(
                brush = Brush.verticalGradient(listOf(Color.White, Color.Transparent)),
                alpha = 0.8f
            )
    )
}
