@file:OptIn(
    ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.map.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.util.Log
import android.widget.TextView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.MapUiState
import com.cmc12th.runway.ui.map.MapViewModel
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.SearchBoxAndTagCategory
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import ted.gun0912.clustering.naver.TedNaverClustering

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(appState: ApplicationState) {

    val mapViewModel: MapViewModel = hiltViewModel()

    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            granted = isGranted
        },
    )
    if (ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        granted = true
    }

    if (granted) {
        MapViewContents(appState = appState, mapViewModel = mapViewModel)
    } else {
        PermissionGranted(launcher)
    }
}


@Composable
private fun PermissionGranted(launcher: ManagedActivityResultLauncher<String, Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            launcher.launch(ACCESS_FINE_LOCATION)
        }) {
            Text(text = "위치 권한을 허용해 주세요.", color = Color.White)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun MapViewContents(
    appState: ApplicationState,
    mapViewModel: MapViewModel,
) {
    val uiState by mapViewModel.mapUiState.collectAsStateWithLifecycle()

    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val localDensity = LocalDensity.current
    val systemUiController = rememberSystemUiController()

    val onSearching = remember {
        mutableStateOf(false)
    }
    val peekHeight = remember {
        mutableStateOf(60.dp)
    }
    val onZoom = remember {
        mutableStateOf(false)
    }
    var topBarHeight by remember {
        mutableStateOf(0.dp)
    }

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

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetPeekHeight = peekHeight.value,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            MapViewBottomSheetContent(appState, screenHeight - topBarHeight)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            /** 네이버 지도 */
            RunwayNaverMap(
                uiState = uiState,
                mapViewModel = mapViewModel,
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
                        .wrapContentHeight()
                        .statusBarsPadding()
                        .onGloballyPositioned { coordinates ->
                            topBarHeight = with(localDensity) { coordinates.size.height.toDp() }
                        }
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
    onMapClick: () -> Unit,
    mapViewModel: MapViewModel,
    uiState: MapUiState,
) {

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(37.5437, 127.0659), 8.0)
    }

    LaunchedEffect(uiState.userPosition) {
//        cameraPositionState.move(
//            CameraUpdate.scrollAndZoomTo(uiState.userPosition, 8.0)
//        )
    }

    DisposableEffect(Unit) {
        mapViewModel.addLocationListener()
        onDispose {
            mapViewModel.removeLocationListener()
        }
    }

    NaverMap(
        modifier = Modifier
            .fillMaxSize(),
        onMapClick = { _, _ ->
            onMapClick()
        },
        cameraPositionState = cameraPositionState
    ) {
        val context = LocalContext.current
        var clusterManager by remember { mutableStateOf<TedNaverClustering<NaverItem>?>(null) }
        DisposableMapEffect(uiState.markerItems) { map ->
            if (clusterManager == null) {
                clusterManager = TedNaverClustering.with<NaverItem>(context, map)
                    .customCluster {

                        TextView(context).apply {
                            text = it.size.toString()
                            background = AppCompatResources.getDrawable(
                                context,
                                R.drawable.circle_clustor
                            )
                            setTextColor(R.color.primary)
                            setTextAppearance(R.style.clustorText)
                            typeface = ResourcesCompat.getFont(this.context,
                                R.font.spoqa_han_sans_neo_bold)
                            setPadding(100, 100, 100, 100)
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
            clusterManager?.addItems(uiState.markerItems)
            onDispose {
                clusterManager?.clearItems()
            }
        }
    }
}

