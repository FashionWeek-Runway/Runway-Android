package com.cmc12th.runway.ui.signin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.Blue100
import com.cmc12th.runway.ui.theme.Body2M
import com.cmc12th.runway.ui.theme.Error_Color
import com.cmc12th.runway.ui.theme.Primary


@Composable
fun BoxScope.RetryContainer(resendMessage: () -> Unit, retryTime: Int) {
    Row(
        modifier = Modifier.align(Alignment.CenterEnd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${retryTime / 60}:${"%02d".format(retryTime % 60)}",
            color = Error_Color,
            fontSize = 14.sp
        )
        WidthSpacer(width = 15.dp)
        Box(modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Blue100)
            .clickable {
                resendMessage()
            }) {
            Text(
                text = "재요청",
                modifier = Modifier.padding(14.dp, 7.dp),
                style = Body2M,
                color = Primary
            )
        }
    }
}