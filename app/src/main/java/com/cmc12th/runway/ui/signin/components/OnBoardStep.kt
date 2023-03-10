package com.cmc12th.runway.ui.signin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.theme.Gray100
import com.cmc12th.runway.ui.theme.Point

@Composable
fun OnBoardStep(step: Int) {
    val steps = MutableList(6) { false }
    (0 until step).forEach { steps[it] = true }
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(Gray100),
    ) {
        steps.forEachIndexed { _, now ->
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .background(if (now) Point else Gray100)
            )
        }
    }
}
