@file:OptIn(
    ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.map.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.util.Log
import android.widget.TextView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.map.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.data.model.SearchType
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.detail.DetailVIewModel
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.MapUiState
import com.cmc12th.runway.ui.map.MapViewModel
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.GpsIcon
import com.cmc12th.runway.ui.map.components.RefreshIcon
import com.cmc12th.runway.ui.map.components.SearchBoxAndTagCategory
import com.cmc12th.runway.ui.map.model.MapStatus
import com.cmc12th.runway.ui.map.model.MovingCameraWrapper
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import ted.gun0912.clustering.naver.TedNaverClustering

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
    val mapUiState by mapViewModel.mapUiState.collectAsStateWithLifecycle()
//    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
//        position = CameraPosition(LatLng(37.5437, 127.0659), 8.0)
//    }

    val detailViewModel: DetailVIewModel = hiltViewModel()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val localDensity = LocalDensity.current
    val systemUiController = rememberSystemUiController()
    val coroutineScope = rememberCoroutineScope()

    val onSearching = remember {
        mutableStateOf(false)
    }


    val peekHeight = remember {
        mutableStateOf(60.dp)
    }

    var topBarHeight by remember {
        mutableStateOf(0.dp)
    }

    val onMarkerClick: (NaverItem) -> Unit = {
        mapViewModel.updateMapStatus(MapStatus.MARKER_CLICKED)
        mapViewModel.updateMarker(it.copy(isClicked = !it.isClicked))
        mapViewModel.mapInfo(it.storeId)
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    val onMapClick: () -> Unit = {
        when (mapUiState.mapStatus) {
            MapStatus.DEFAULT -> {
            }
            MapStatus.ZOOM -> {
            }
            MapStatus.LOCATION_SEARCH -> {
            }
            MapStatus.SHOP_SEARCH -> {
            }
            MapStatus.SEARCH_ZOOM -> {
            }
            MapStatus.MARKER_CLICKED -> {
                mapViewModel.mapScrollInfo(appState.cameraPositionState.position.target)
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
            else -> {}
        }
        mapViewModel.onMapClick()
    }

    /** 맵 인터렉션 관리 */
    ManageMapStatus(
        mapUiState.mapStatus,
        systemUiController,
        onSearching,
        peekHeight,
        appState,
        bottomSheetScaffoldState,
    )

    val setMapStatusDefault: () -> Unit = {
        mapViewModel.updateMapStatus(MapStatus.DEFAULT)
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }
    val setMapStatusOnSearch = { mapViewModel.updateMapStatus(MapStatus.SEARCH_TAB) }

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        sheetPeekHeight = peekHeight.value,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            MapViewBottomSheetContent(
                appState = appState,
                contents = mapUiState.bottomSheetContents,
                screenHeight = screenHeight - topBarHeight + BOTTOM_NAVIGATION_HEIGHT,
                isFullScreen = mapUiState.mapStatus == MapStatus.LOCATION_SEARCH,
                isExpanded = bottomSheetScaffoldState.bottomSheetState.targetValue == BottomSheetValue.Expanded,
                setMapStatusDefault = setMapStatusDefault,
                setMapStatusOnSearch = setMapStatusOnSearch
            ) { mapViewModel.onDetail.value = true }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            /** 네이버 지도 */
            RunwayNaverMap(
                cameraPositionState = appState.cameraPositionState,
                uiState = mapUiState,
                mapViewModel = mapViewModel,
                onMapClick = onMapClick,
                onMarkerClick = onMarkerClick,
                onSearching = onSearching,
            )

            RefreshIcon(
                visibility = mapUiState.mapStatus == MapStatus.DEFAULT,
                yOffset = topBarHeight + 12.dp,
                onClick = {
                    mapViewModel.mapFiltering(appState.cameraPositionState.position.target)
                }
            )

            GpsIcon(
                visiblitiy = mapUiState.mapStatus.isGpsIconVisibility() || bottomSheetScaffoldState.bottomSheetState.targetValue == BottomSheetValue.Expanded,
                offsetY = with(localDensity) { bottomSheetScaffoldState.bottomSheetState.offset.value.toDp() }
            ) {
                coroutineScope.launch {
                    appState.cameraPositionState.animate(
                        update = CameraUpdate.scrollAndZoomTo(
                            mapUiState.userPosition,
                            15.0
                        ),
                    )
                }
            }

            /** 검색 및 필터 */
            AnimatedVisibility(
                visible = mapUiState.mapStatus == MapStatus.DEFAULT,
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
                        isBookmarked = mapUiState.isBookmarked,
                        categoryItems = mapUiState.categoryItems,
                        updateCategoryTags = { mapViewModel.updateCategoryTags(it) },
                        updateIsBookmarked = { mapViewModel.updateIsBookmarked(it) },
                        onSearch = { mapViewModel.updateMapStatus(MapStatus.SEARCH_TAB) }
                    )
                    BottomGradient(20.dp)
                }
            }

            /** 검색 결과 탑바 */
            AnimatedVisibility(
                visible = mapUiState.mapStatus.searchResultTopBarVisiblity(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SearchResultBar(
                    modifier = Modifier.padding(20.dp, 16.dp),
                    setMapStatusDefault = setMapStatusDefault,
                    setMapStatusOnSearch = setMapStatusOnSearch
                )
            }

        }
    }


    if (mapViewModel.onDetail.value) {
        DetailScreen(appState = appState,
            idx = 1,
            detailVIewModel = detailViewModel,
            onBackPress = { mapViewModel.onDetail.value = false }
        )
    }

}

@Composable
private fun ManageMapStatus(
    mapStatus: MapStatus,
    systemUiController: SystemUiController,
    onSearching: MutableState<Boolean>,
    peekHeight: MutableState<Dp>,
    appState: ApplicationState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
) {
    LaunchedEffect(key1 = mapStatus) {
        Log.i("mapStatus", mapStatus.toString())
        systemUiController.setNavigationBarColor(Color.White)
        when (mapStatus) {
            /** 기본 상태 */
            MapStatus.DEFAULT -> {
                onSearching.value = false
                systemUiController.setSystemBarsColor(Color.White)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
                appState.changeBottomBarVisibility(true)
            }
            /** 한번 클릭했을 때 */
            MapStatus.ZOOM -> {
                onSearching.value = false
                systemUiController.setSystemBarsColor(Color.Transparent)
                systemUiController.setNavigationBarColor(Color.Transparent)
                peekHeight.value = 0.dp
                appState.changeBottomBarVisibility(false)
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            /** 검색 탭에 들어갔을 때 */
            MapStatus.SEARCH_TAB -> {
                onSearching.value = true
                bottomSheetScaffoldState.bottomSheetState.collapse()
                appState.changeBottomBarVisibility(false)
                peekHeight.value = 0.dp
            }
            /** 지역 클릭 */
            MapStatus.LOCATION_SEARCH -> {
                onSearching.value = false
                appState.changeBottomBarVisibility(false)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
            /** 매장 클릭 */
            MapStatus.SHOP_SEARCH -> {
                onSearching.value = false
                appState.changeBottomBarVisibility(false)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 200.dp
            }
            MapStatus.SEARCH_ZOOM -> {
                systemUiController.setNavigationBarColor(Color.Transparent)
                peekHeight.value = 0.dp
                appState.changeBottomBarVisibility(false)
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            /** 장소 검색에서 마커 클릭 */
            MapStatus.LOCATION_SEARCH_MARKER_CLICKED -> {
                bottomSheetScaffoldState.bottomSheetState.expand()
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
            /** 마커 클릭 */
            MapStatus.MARKER_CLICKED -> {
                bottomSheetScaffoldState.bottomSheetState.expand()
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
        }
    }
}


@Composable
fun SearchResultBar(
    modifier: Modifier = Modifier,
    setMapStatusDefault: () -> Unit,
    setMapStatusOnSearch: () -> Unit,
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RunwayIconButton(drawable = R.drawable.ic_left_runway) {
                setMapStatusOnSearch()
            }
            Text(
                text = "매장이나 장소 이름명",
                style = Body1B,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                setMapStatusDefault()
            }, modifier = Modifier.size(24.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_baseline_small),
                    contentDescription = "IC_CLOSE_BASELINE_SMALL",
                    tint = Color.Black
                )
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
    cameraPositionState: CameraPositionState,
    onMarkerClick: (NaverItem) -> Unit,
    onSearching: MutableState<Boolean>,
) {

    LaunchedEffect(key1 = cameraPositionState.position) {
        // Log.i("dlgocks1", cameraPositionState.position.target.toString())
    }

    LaunchedEffect(key1 = uiState.movingCameraPosition) {
        when (uiState.movingCameraPosition) {
            MovingCameraWrapper.DEFAULT -> {}
            is MovingCameraWrapper.MOVING -> {
                cameraPositionState.animate(
                    update = CameraUpdate.scrollAndZoomTo(
                        LatLng(uiState.movingCameraPosition.location), 12.0
                    )
                )
                mapViewModel.updateMovingCamera(MovingCameraWrapper.DEFAULT)
            }
        }
    }

    val context = LocalContext.current
    var clusterManager by remember { mutableStateOf<TedNaverClustering<NaverItem>?>(null) }

    val resetSelectedMakrer: () -> Unit = {
        mapViewModel.resetSelectedMarkers()
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
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            locationTrackingMode = LocationTrackingMode.None,
        ),
    ) {
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
                            typeface = ResourcesCompat.getFont(
                                this.context,
                                R.font.spoqa_han_sans_neo_bold
                            )
                            setPadding(100, 100, 100, 100)
                        }
                    }.customMarker {
                        Marker().apply {
                            icon =
                                OverlayImage.fromResource(R.drawable.ic_fill_map_marker_default_24)
                            width = if (it.isClicked) 120 else 60
                            height = if (it.isClicked) 120 else 60
                            captionText = it.title
                        }
                    }
                    .clusterAnimation(false)
                    .markerClickListener {
                        onMarkerClick(it)
                    }
                    .make()
            }

            clusterManager?.addItems(uiState.markerItems)

            onDispose {
                clusterManager?.clearItems()
            }
        }

        Marker(
            state = MarkerState(position = uiState.userPosition),
            icon = OverlayImage.fromResource(R.drawable.ic_map_user),
            height = 24.dp,
            width = 24.dp,
            onClick = {
                true
            }
        )
    }

    /** 검색 스크린을 위에 깔아버리기 */
    AnimatedVisibility(
        visible = onSearching.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        MapSearchScreen(
            onShopSearch = {
                mapViewModel.updateMapStatus(MapStatus.SHOP_SEARCH)
//                mapViewModel.mapInfo()
                mapViewModel.addRecentStr(
                    it.storeName,
                    SearchType(it.storeId, SearchType.STORE_TYPE)
                )
            },
            onBackPrseed = {
                mapViewModel.updateSearchText(TextFieldValue(""))
                onSearching.value = false
                mapViewModel.updateMapStatus(MapStatus.DEFAULT)
            },
            onLocationSearch = {
                mapViewModel.updateMapStatus(MapStatus.LOCATION_SEARCH)
            },
            mapViewModel = mapViewModel
        )
    }
}

