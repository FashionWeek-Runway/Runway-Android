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
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray200)
                .aspectRatio(1.6f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(it.storeImg)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IMG_SELECTED_IMG",
            contentScale = ContentScale.Crop,
        )
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