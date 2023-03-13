package com.cmc12th.runway.ui.mypage.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1

@Composable
fun ColumnScope.EmptyStorage(
    title: String,
    @DrawableRes drawableResId: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 85.dp)
            .weight(1f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = "IMG_EMPTY_BOOKMARK",
            modifier = Modifier.size(182.dp, 163.dp),
            contentScale = ContentScale.Crop
        )
        HeightSpacer(height = 30.dp)
        Text(text = title, style = Body1, color = Black)
    }
}