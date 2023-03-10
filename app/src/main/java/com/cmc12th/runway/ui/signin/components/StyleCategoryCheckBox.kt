package com.cmc12th.runway.ui.signin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.*

@Composable
fun StyleCategoryCheckBox(
    isSelected: Boolean,
    color: Color,
    onClicked: () -> Unit,
    title: String,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, if (isSelected) Primary else Gray300, RoundedCornerShape(5.dp))
            .background(color)
            .clickable {
                onClicked()
            }
    ) {
        Row(
            modifier = Modifier.padding(15.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "IC_CHECK",
                modifier = Modifier.size(18.dp),
                tint = if (isSelected) Point else Gray300
            )

            WidthSpacer(width = 10.dp)
            Text(
                text = title,
                style = if (isSelected) Body1B else Body1,
                color = if (isSelected) White else Gray600
            )
        }
    }
}