@file:OptIn(ExperimentalNaverMapApi::class)

package com.cmc12th.runway.ui.map.view

import android.util.Log
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.signin.components.StyleCategoryCheckBox
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.CATEGORYS
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import ted.gun0912.clustering.naver.TedNaverClustering

@Composable
fun MapScreen() {

    val items = listOf<NaverItem>(
        NaverItem(37.542258155004774, 127.05653993251198).apply { title = "아더 성수스페이스" },
        NaverItem(37.541397589495894, 127.06127752698968).apply { title = "세터하우스" },
        NaverItem(37.54122227689786, 127.06053623897719).apply { title = "옵스큐라" },
        NaverItem(37.540791, 127.096306),
        NaverItem(37.550791, 127.076306),
        NaverItem(37.560791, 127.066306),
        NaverItem(37.550791, 127.166306),
        NaverItem(37.540791, 127.176306),
        NaverItem(37.520791, 127.166306),
        NaverItem(37.510791, 127.016306),
        NaverItem(37.570791, 127.046306),
    )

    val onSearching = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        /** 네이버 지도 */
        RunwayNaverMap(items)
        /** 검색 및 필터 */
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            NaverMapSearch(onSearch = { onSearching.value = true })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(listOf(Color.White, Color.Transparent)),
                        alpha = 0.8f
                    )
            )
        }

        /** 검색 스크린을 위에 깔아버리기 */
        AnimatedVisibility(
            visible = onSearching.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MapSearchScreen {
                onSearching.value = false
            }
        }
    }
}

@Composable
private fun NaverMapSearch(onSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Gray300), RoundedCornerShape(4.dp))
                .clickable {
                    onSearch()
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "지역, 매장명 검색", style = Body1, color = Gray300)
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = "IC_SEARCH",
                    modifier = Modifier.size(24.dp),
                    tint = Gray700
                )
            }
        }
        HeightSpacer(height = 15.dp)
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CATEGORYS) {
                val surfaceColor: State<Color> = animateColorAsState(
//                    if (categoryTag.isSelected) Primary else
                    White
                )
                StyleCategoryCheckBox(
                    isSelected = false,
                    color = surfaceColor.value,
                    onClicked = { /*TODO*/ },
                    title = it
                )
                WidthSpacer(width = 10.dp)
            }
        }
    }
}


/** NaverMap Compose
 *  https://github.com/fornewid/naver-map-compose */
@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun RunwayNaverMap(items: List<NaverItem>) {
    NaverMap(
        modifier = Modifier
            .fillMaxSize()
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
                            captionText = it.title ?: "제목"
                        }
                    }.make()
            }
            clusterManager?.addItems(items)
            onDispose {
                clusterManager?.clearItems()
            }
        }


    }
}

