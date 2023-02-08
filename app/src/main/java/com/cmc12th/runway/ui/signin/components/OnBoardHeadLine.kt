package com.cmc12th.runway.ui.signin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.theme.HeadLine3

@Composable
fun OnBoardHeadLine(main: String, sub: String) {
    Row(modifier = Modifier.padding(top = 20.dp)) {
        Text(text = main, style = HeadLine3)
        Text(text = sub, fontSize = 20.sp, fontWeight = FontWeight.Normal)
    }
}