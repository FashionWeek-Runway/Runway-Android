package com.cmc12th.runway.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R

@Composable
fun BackIcon(onClick: () -> Unit = {}) {
    IconButton(
        modifier = Modifier
            .size(24.dp),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_left_runway),
            contentDescription = "IC_ARROW"
        )
    }
}
