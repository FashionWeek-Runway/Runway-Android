package com.cmc12th.runway.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R

@Composable
fun RunwayIconButton(
    size: Dp = 24.dp,
    @DrawableRes drawable: Int,
    tint: Color = Color.Unspecified,
    onCLick: () -> Unit = {},
) {
    IconButton(
        onClick = onCLick,
        modifier = Modifier.size(size)
    ) {
        Icon(
            painter = painterResource(id = drawable),
            contentDescription = "IC_DUMMY",
            tint = tint
        )
    }
}
