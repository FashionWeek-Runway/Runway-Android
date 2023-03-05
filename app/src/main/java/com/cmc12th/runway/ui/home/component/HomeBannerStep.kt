package com.cmc12th.runway.ui.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.theme.Gray100_50
import com.cmc12th.runway.ui.theme.Point

@Composable
fun HomeBannerStep(modifier: Modifier, step: Int = 0, maxSize: Int = 1) {
    val steps = MutableList(maxSize.coerceAtLeast(1)) { false }
    (0 until step).forEach { steps[it] = true }
    Row(
        modifier = modifier
            .fillMaxWidth(1f)
            .background(Gray100_50),
    ) {
        steps.forEachIndexed { _, now ->
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(if (now) Point else Gray100_50)
            )
        }
    }
}
