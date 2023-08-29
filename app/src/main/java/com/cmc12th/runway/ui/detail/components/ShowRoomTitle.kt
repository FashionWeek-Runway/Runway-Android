@file:OptIn(
    ExperimentalLayoutApi::class
)

package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cmc12th.domain.model.response.store.StoreDetail
import com.cmc12th.runway.ui.components.NavigateIcon
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.HeadLine2
import com.cmc12th.runway.utils.moveToNaverMap


@Composable
fun ShowRoomTitle(storeDetail: StoreDetail) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (storeDetail.storeName.isEmpty()) {
                Box(
                    modifier = Modifier
                        .size(180.dp, 24.dp)
                        .background(Gray300)
                )
            } else {
                Text(text = storeDetail.storeName, style = HeadLine2, color = Color.Black)
            }
            FlowRow {
                storeDetail.category.forEach {
                    ShowRoomTag(it)
                }
            }
        }
        NavigateIcon {
            moveToNaverMap(
                context = context,
                latitude = storeDetail.latitude,
                longitude = storeDetail.longitude,
                storeName = storeDetail.storeName
            )
        }
    }
}
