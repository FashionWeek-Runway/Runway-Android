package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.theme.Blue200
import com.cmc12th.runway.ui.theme.Blue600
import com.cmc12th.runway.ui.theme.Button2

@Composable
fun ShowRoomTag(categoryTag: String) {
    Box(modifier = Modifier.padding(top = 4.dp, end = 6.dp)) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Blue200), RoundedCornerShape(4.dp))
                .background(Color(0x50E6EBFF)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "# $categoryTag", style = Button2, color = Blue600,
                modifier = Modifier
                    .padding(8.dp, 6.dp)
            )
        }
    }
}