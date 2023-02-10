package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.theme.Error_Color

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier.offset(y = 5.dp), color = Error_Color, fontSize = 14.sp
    )
}