@file:OptIn(ExperimentalFoundationApi::class)

package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.ui.map.components.TopGradient
import com.cmc12th.runway.ui.theme.Gray200

@Composable
fun ShowRoomBanner(storeDetail: StoreDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        HorizontalPager(
            pageCount = storeDetail.imgUrlList.size,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .aspectRatio(1.2f)
        ) {
            AsyncImage(
                modifier = Modifier
                    .background(Gray200)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(storeDetail.imgUrlList[it])
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "SHOP_IMAGE",
                contentScale = ContentScale.Crop,
            )
        }

        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 20.dp, alpha = 1f)
    }
}
