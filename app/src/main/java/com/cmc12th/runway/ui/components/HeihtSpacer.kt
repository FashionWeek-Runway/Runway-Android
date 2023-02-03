package com.cmc12th.runway.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnScope.HeightSpacer(height: Dp) {
    Spacer(modifier = androidx.compose.ui.Modifier.height(height))
}