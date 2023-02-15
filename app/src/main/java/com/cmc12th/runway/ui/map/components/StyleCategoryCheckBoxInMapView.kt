package com.cmc12th.runway.ui.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.cmc12th.runway.ui.theme.*

@Composable
fun StyleCategoryCheckBoxInMapView(
    isSelected: Boolean,
    color: Color,
    onClicked: () -> Unit,
    title: String,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, if (isSelected) Primary else Gray200, RoundedCornerShape(5.dp))
            .background(color)
            .clickable {
                onClicked()
            }
    ) {
        Row(
            modifier = Modifier.padding(15.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = if (isSelected) Body1B else Body1,
                color = if (isSelected) White else Gray600
            )
        }
    }
}
