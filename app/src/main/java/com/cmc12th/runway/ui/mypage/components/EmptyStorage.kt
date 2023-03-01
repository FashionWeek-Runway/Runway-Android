package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1

@Composable
fun ColumnScope.EmptyStorage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 85.dp)
            .weight(1f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IMG_DUMMY",
            modifier = Modifier.size(100.dp)
        )
        HeightSpacer(height = 30.dp)
        Text(text = "마음에 드는 매장을 저장해 보세요", style = Body1, color = Black)
    }
}