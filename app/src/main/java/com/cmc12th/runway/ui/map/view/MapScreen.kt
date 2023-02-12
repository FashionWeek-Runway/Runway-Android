@file:OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.map.view

import android.util.Log
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.NaverMapSearch
import com.cmc12th.runway.ui.theme.*
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import ted.gun0912.clustering.naver.TedNaverClustering

@OptIn(ExperimentalMaterialApi::class)
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

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(key1 = bottomSheetScaffoldState.bottomSheetState.currentValue) {
        Log.i("dlgocks1", bottomSheetScaffoldState.bottomSheetState.currentValue.toString())
    }
    LaunchedEffect(key1 = onSearching.value) {
        if (onSearching.value) bottomSheetScaffoldState.bottomSheetState.collapse()
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = if (onSearching.value) 0.dp else 60.dp,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight - 200.dp)
                    .padding(20.dp, 10.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .height(3.dp)
                        .width(36.dp)
                        .background(Gray200)
                        .align(Alignment.CenterHorizontally)
                )
                HeightSpacer(height = 10.dp)
                Text(text = "성수동 둘러보기", style = Body1M, color = Color.Black)
                AnimatedVisibility(
                    visible = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Text(text = "보텀쉿테스트")
                        Text(text = "보텀쉿테스트")
                        Text(text = "보텀쉿테스트")
                        Text(text = "보텀쉿테스트")
                    }
                }
            }
        }
    ) {
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
                NaverMapSearch(onSearch = {
                    onSearching.value = true
                })
                BottomGradient()
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

