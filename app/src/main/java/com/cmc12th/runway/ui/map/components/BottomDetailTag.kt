package com.cmc12th.runway.ui.map.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.Blue200
import com.cmc12th.runway.ui.theme.Blue600
import com.cmc12th.runway.ui.theme.Button2

@Composable
fun BottomDetailTag(it: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = Color(0x50E6EBFF))
            .border(
                BorderStroke(1.dp, Blue200),
                RoundedCornerShape(4.dp)
            )
    ) {
        Text(
            text = "# $it",
            style = Button2,
            color = Blue600,
            modifier = Modifier.padding(8.dp, 6.dp)
        )
    }
    WidthSpacer(width = 8.dp)
}
