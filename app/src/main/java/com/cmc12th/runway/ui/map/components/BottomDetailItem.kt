@file:OptIn(ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.map.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.domain.model.response.map.MapInfoItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.theme.Caption2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.HeadLine4
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.utils.isPackageInstalled


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

    // 해당 좌표로 네이버 지도 길찾기 인텐트를 보낸다.
    val moveToNaverMap = {
        val naverMapPackage = "com.nhn.android.nmap"
        val packageManager = context.packageManager
        val isNaverMapInstalled = isPackageInstalled(naverMapPackage, packageManager)
        val isKakaoMapInstalled = isPackageInstalled("net.daum.android.map", packageManager)

        // 네이버 맵 URL 스킴 : https://guide-gov.ncloud-docs.com/docs/naveropenapiv3-maps-url-scheme-url-scheme
        // 카카오 맵 URL 스킴 : https://apis.map.kakao.com/android/guide/#urlscheme
        if (isNaverMapInstalled) {
            val url =
                "nmap://search?lat=${mapInfoItem.latitude}&lng=${mapInfoItem.longitude}&query=${mapInfoItem.storeName}&appname=com.cmc12th.runway"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } else {
            if (isKakaoMapInstalled) {
                val url =
                    "daummaps://search?q=${mapInfoItem.storeName}&p=${mapInfoItem.latitude},${mapInfoItem.longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        }
    }


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
                Column(
                    modifier = Modifier
                        .clickable {
                            moveToNaverMap()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .background(color = Primary, shape = RoundedCornerShape(size = 15.dp))
                            .padding(start = 5.dp, top = 5.dp, end = 5.dp, bottom = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_trace_20),
                            contentDescription = "IC_TRACE",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "길찾기",
                        modifier = Modifier.padding(top = 3.dp),
                        style = Caption2,
                        color = Primary
                    )
                }
            }
        }
    }
}