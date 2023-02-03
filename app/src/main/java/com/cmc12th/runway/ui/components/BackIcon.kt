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
fun BackIcon() {
    IconButton(
        modifier = Modifier
            .size(24.dp),
        onClick = {},
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "IC_BASELINE_ARROW_BACK")
    }
}
