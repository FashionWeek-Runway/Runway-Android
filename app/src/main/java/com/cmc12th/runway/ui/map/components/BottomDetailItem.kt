@file:OptIn(ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.map.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.domain.model.response.map.MapInfoItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.NavigateIcon
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.HeadLine4
import com.cmc12th.runway.utils.isPackageInstalled
import com.cmc12th.runway.utils.moveToNaverMap


@Composable
@Preview
fun BottomDetailItem(
    navigateToDetail: (id: Int, storeName: String) -> Unit = { _, _ -> },
    mapInfoItem: MapInfoItem = MapInfoItem(
        storeId = 1,
        storeName = "스타벅스",
        storeImg = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg",
        category = listOf("카페", "커피", "디저트"),
        latitude = 37.5665,
        longitude = 126.9780,
    ),
    isNavigationButtonEnabled: Boolean = false,
) {
    val context = LocalContext.current

    Column(modifier = Modifier
        .clickable {
            navigateToDetail(mapInfoItem.storeId, mapInfoItem.storeName)
        }) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray200)
                .aspectRatio(1.6f),
            model = mapInfoItem.storeImg,
            contentDescription = "IMG_SELECTED_IMG",
            contentScale = ContentScale.Crop,
        ) { requestBuilder ->
            requestBuilder.placeholder(R.color.gray200).signature(ObjectKey(mapInfoItem.storeImg))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 6.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = mapInfoItem.storeName,
                    style = HeadLine4,
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(mapInfoItem.category) {
                        BottomDetailTag(it)
                    }
                }
            }
            if (isNavigationButtonEnabled) {
                NavigateIcon {
                    moveToNaverMap(
                        context = context,
                        latitude = mapInfoItem.latitude,
                        longitude = mapInfoItem.longitude,
                        storeName = mapInfoItem.storeName
                    )
                }
            }
        }
    }
}


