package com.cmc12th.runway.ui.map.components

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
import com.cmc12th.runway.ui.theme.HeadLine4


@Composable
fun BottomDetailItem(
    navigateToDetail: (id: Int, storeName: String) -> Unit,
    it: MapInfoItem
) {
    Column(modifier = Modifier.clickable {
        navigateToDetail(it.storeId, it.storeName)
    }) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1.6f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(it.storeImg)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.img_dummy),
            error = painterResource(id = R.drawable.img_dummy),
            contentDescription = "ASDas",
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