@file:OptIn(ExperimentalNaverMapApi::class)

package com.cmc12th.runway.ui.map

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.cmc12th.domain.model.NaverItem
import com.cmc12th.runway.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import ted.gun0912.clustering.naver.TedNaverClustering


@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen() {

    val items = listOf<NaverItem>(
        NaverItem(37.532600, 127.024612),
        NaverItem(37.390791, 127.096306),
        NaverItem(37.540791, 127.096306),
        NaverItem(37.550791, 127.076306),
        NaverItem(37.560791, 127.066306),
        NaverItem(37.510791, 127.016306),
        NaverItem(37.570791, 127.046306),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        /** NaverMap Compose
         *  https://github.com/fornewid/naver-map-compose */
        NaverMap(
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            var clusterManager by remember { mutableStateOf<TedNaverClustering<NaverItem>?>(null) }
            DisposableMapEffect(items) { map ->
                if (clusterManager == null) {
                    clusterManager = TedNaverClustering.with<NaverItem>(context, map)
                        .customCluster {
                            TextView(context).apply {
                                text = "${it.size}개"
                                background = AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.circle_clustor
                                )
                                setPadding(150, 150, 150, 150)
                            }
                        }.customMarker {
                            Marker().apply {
                                icon = OverlayImage.fromResource(R.drawable.ic_nav_mypage_on)
                                width = 60
                                height = 86
                            }
                        }.make()
                }
                clusterManager?.addItems(items)
                onDispose {
                    clusterManager?.clearItems()
                }
            }
//            Marker(
//                state = MarkerState(position = LatLng()),
//                captionText = "Marker in Seoul"
//            )
//            Marker(
//                state = MarkerState(position = LatLng()),
//                captionText = "Marker in Pangyo"
//            )
        }
    }
}

