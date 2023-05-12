@file:OptIn(ExperimentalLayoutApi::class)

package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cmc12th.domain.model.response.store.StoreDetail
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.HeadLine2


@Composable
fun ShowRoomTitle(storeDetail: StoreDetail) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 0.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        if (storeDetail.storeName.isEmpty()) {
            Box(modifier = Modifier
                .size(180.dp, 24.dp)
                .background(Gray300))
        } else {
            Text(text = storeDetail.storeName, style = HeadLine2, color = Color.Black)
        }
        FlowRow {
            storeDetail.category.forEach {
                ShowRoomTag(it)
            }
        }
    }
}
