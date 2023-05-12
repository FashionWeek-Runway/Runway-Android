@file:OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.domain.model.response.store.StoreDetail
import com.cmc12th.runway.R
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
            Box {
                GlideImage(
                    modifier = Modifier
                        .background(Gray200)
                        .fillMaxSize(),
                    model = storeDetail.imgUrlList[it],
                    contentDescription = "SHOP_IMAGE",
                    contentScale = ContentScale.Crop,
                ) { requestBuilder ->
                    requestBuilder.placeholder(R.color.gray200)
                        .signature(ObjectKey(storeDetail.imgUrlList[it]))
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(
                            brush = Brush.verticalGradient(listOf(Color.Black, Color.Transparent)),
                            alpha = 0.5f
                        )
                )
            }

        }

        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 20.dp, alpha = 1f)
    }
}
