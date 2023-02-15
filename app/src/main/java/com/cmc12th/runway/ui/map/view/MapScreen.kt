@file:OptIn(
    ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.map.view

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.MapViewModel
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.SearchBoxAndTagCategory
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import ted.gun0912.clustering.naver.TedNaverClustering

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(appState: ApplicationState) {

    val mapViewModel: MapViewModel = hiltViewModel()
    val onSearching = remember {
        mutableStateOf(false)
    }
    val peekHeight = remember {
        mutableStateOf(60.dp)
    }
    val onZoom = remember {
        mutableStateOf(false)
    }

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val uiState by mapViewModel.mapUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        // TODO 사용자 정보 가져오기 및 위치에 따른 장소 가져오기
//        mapViewModel.stores()
    }

    LaunchedEffect(key1 = onSearching.value) {
        if (onSearching.value) {
            bottomSheetScaffoldState.bottomSheetState.collapse()
            appState.changeBottomBarVisibility(false)
            peekHeight.value = 0.dp
        } else {
            appState.changeBottomBarVisibility(true)
            peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
        }
    }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = onZoom.value) {
        if (onZoom.value) {
            systemUiController.setSystemBarsColor(Color.Transparent)
            systemUiController.setNavigationBarColor(Color.Transparent)
            peekHeight.value = 0.dp
            appState.changeBottomBarVisibility(false)
            bottomSheetScaffoldState.bottomSheetState.collapse()
        } else {
            systemUiController.setSystemBarsColor(Color.White)
            peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            appState.changeBottomBarVisibility(true)
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetPeekHeight = peekHeight.value,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            MapViewBottomSheetContent(appState, screenHeight)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            /** 네이버 지도 */
            RunwayNaverMap(
                items = mapViewModel._markerItems.value,
                onMapClick = {
                    onZoom.value = !onZoom.value
                })

            /** 검색 및 필터 */
            AnimatedVisibility(
                visible = !onZoom.value,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                ) {
                    SearchBoxAndTagCategory(
                        isBookmarked = uiState.isBookmarked,
                        categoryItems = uiState.categoryItems,
                        updateCategoryTags = { mapViewModel.updateCategoryTags(it) },
                        updateIsBookmarked = { mapViewModel.updateIsBookmarked(it) },
                        onSearch = { onSearching.value = true }
                    )
                    BottomGradient(20.dp)
                }
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
private fun RunwayNaverMap(
    items: List<NaverItem>,
    onMapClick: () -> Unit,
) {
    NaverMap(
        modifier = Modifier
            .fillMaxSize(),
        onMapClick = { _, _ ->
            onMapClick()
        }
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

