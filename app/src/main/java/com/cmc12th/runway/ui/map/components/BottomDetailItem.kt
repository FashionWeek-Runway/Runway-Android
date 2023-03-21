@file:OptIn(ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.map.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.HeadLine4


@Composable
fun BottomDetailItem(
    navigateToDetail: (id: Int, storeName: String) -> Unit,
    it: MapInfoItem,
) {
    Column(modifier = Modifier
        .clickable {
            navigateToDetail(it.storeId, it.storeName)
        }) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray200)
                .aspectRatio(1.6f),
            model = it.storeImg,
            contentDescription = "IMG_SELECTED_IMG",
            contentScale = ContentScale.Crop,
        ) { requestBuilder ->
            requestBuilder.placeholder(R.color.gray200).signature(ObjectKey(it.storeImg))
        }
        Text(
            text = it.storeName,
            style = HeadLine4,
            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
        )
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(it.category) {
                BottomDetailTag(it)
            }
        }
    }
}