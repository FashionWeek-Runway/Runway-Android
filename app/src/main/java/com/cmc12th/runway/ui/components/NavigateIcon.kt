package com.cmc12th.runway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.theme.Blue200
import com.cmc12th.runway.ui.theme.Blue50
import com.cmc12th.runway.ui.theme.Blue600
import com.cmc12th.runway.ui.theme.Caption2

@Composable
fun NavigateIcon(moveToNaverMap: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                moveToNaverMap()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .background(color = Blue50, shape = RoundedCornerShape(size = 15.dp))
                .border(
                    width = 0.5.dp,
                    color = Blue200,
                    shape = RoundedCornerShape(size = 15.dp)
                )
                .padding(
                    start = 5.dp, top = 5.dp, end = 5.dp, bottom = 5.dp
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_trace_20),
                contentDescription = "IC_TRACE",
                tint = Blue600,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "길찾기",
            modifier = Modifier.padding(top = 3.dp),
            style = Caption2,
            color = Blue600
        )
    }
}