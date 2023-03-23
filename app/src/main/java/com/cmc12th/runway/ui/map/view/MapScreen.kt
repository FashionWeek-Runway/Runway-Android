@file:OptIn(
    ExperimentalNaverMapApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.map.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.data.model.SearchType
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.MapUiState
import com.cmc12th.runway.ui.map.MapViewModel
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
import com.cmc12th.runway.ui.map.components.*
import com.cmc12th.runway.ui.map.model.BottomSheetContent
import com.cmc12th.runway.ui.map.model.MapStatus
import com.cmc12th.runway.ui.map.model.MovingCameraWrapper
import com.cmc12th.runway.ui.map.model.NaverItem
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.viewLogEvent
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

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
            if (isGranted) {
                mapViewModel.addLocationListener()
            } else {
                appState.showSnackbar("위치정보 권한을 허가해 주세요.")
                MapViewModel.initialMarkerLoadFlag = false
                mapViewModel.initialLodingStatus.value = false
            }
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
    LaunchedEffect(key1 = Unit) {
        viewLogEvent("MapScreen")
        if (!granted) {
            launcher.launch(ACCESS_FINE_LOCATION)
        }
    }
    MapViewContents(appState = appState, mapViewModel = mapViewModel)
//    PermissionGranted(launcher)

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

    val expandBottomSheet: () -> Unit = {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    val collapsBottomSheet: () -> Unit = {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val onMarkerClick: (NaverItem) -> Unit = {
        if (!mapUiState.mapStatus.onSearch()) {
            mapViewModel.saveTempDatas()
            mapViewModel.updateMapStatus(MapStatus.MARKER_CLICKED)
            mapViewModel.updateMarker(it.copy(isClicked = !it.isClicked))
            mapViewModel.mapInfo(it.storeId)
        } else if (mapUiState.mapStatus == MapStatus.LOCATION_SEARCH) {
            mapViewModel.saveLocationTempDatas()
            mapViewModel.updateMapStatus(MapStatus.LOCATION_SEARCH_MARKER_CLICKED)
            mapViewModel.updateMarker(it.copy(isClicked = !it.isClicked))
            mapViewModel.mapInfo(it.storeId)
        }
        mapViewModel.updateMovingCamera(
            MovingCameraWrapper.MOVING(
                Location("SelectedMarker").apply {
                    latitude = it.position.latitude
                    longitude = it.position.longitude
                }
            )
        )
    }

    val onMapClick: () -> Unit = {
        when (mapUiState.mapStatus) {
            MapStatus.MARKER_CLICKED -> {
                collapsBottomSheet()
            }
            MapStatus.LOCATION_SEARCH_MARKER_CLICKED -> {
                collapsBottomSheet()
            }
            else -> {
                // Do Nothing
            }
        }
        mapViewModel.onMapClick()
        collapsBottomSheet()
    }

    val refershIconVisiblity = remember {
        mutableStateOf(true)
    }

    val setMapStatusDefault: () -> Unit = {
        mapViewModel.updateMapStatus(MapStatus.DEFAULT)
        mapViewModel.loadTempDatas()
        collapsBottomSheet()
    }

    val setMapStatusOnSearch = {
        mapViewModel.updateMapStatus(MapStatus.SEARCH_TAB)
    }

    /** 맵 인터렉션 관리 */
    ManageMapStatus(
        mapStatus = mapUiState.mapStatus,
        systemUiController = systemUiController,
        onSearching = onSearching,
        peekHeight = peekHeight,
        changeBottomBarVisibility = {
            appState.changeBottomBarVisibility(it)
        },
        expandBottomSheet = expandBottomSheet,
        collapsBottomSheet = collapsBottomSheet,
    )

    val paddingValues = WindowInsets.systemBars.asPaddingValues() // 시스템바 패딩 계산

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
                screenHeight = screenHeight + BOTTOM_NAVIGATION_HEIGHT - (topBarHeight + paddingValues.calculateBottomPadding()),
                isLocationSearch = mapUiState.mapStatus == MapStatus.LOCATION_SEARCH,
                isExpandedTagetValue = bottomSheetScaffoldState.bottomSheetState.targetValue == BottomSheetValue.Expanded,
                isExpanded = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                setMapStatusDefault = setMapStatusDefault,
                setMapStatusOnSearch = setMapStatusOnSearch,
                navigateToDetail = { id, storeName ->
                    mapViewModel.onDetail.value = DetailState(true, id, storeName)
                },
            )
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
                updateRefershIconVisiblity = { refershIconVisiblity.value = it },
                expandBottomSheet = { expandBottomSheet() },
                onMapLoaded = {
                    if (MapViewModel.initialMarkerLoadFlag) {
                        if (mapUiState.userPosition != DEFAULT_LATLNG) {
                            MapViewModel.initialMarkerLoadFlag = false
                            mapViewModel.initialLodingStatus.value = false
                            mapViewModel.mapFiltering(
                                LatLng(
                                    mapUiState.userPosition.latitude,
                                    mapUiState.userPosition.longitude
                                )
                            )
                            mapViewModel.mapScrollInfoPaging(
                                LatLng(
                                    mapUiState.userPosition.latitude,
                                    mapUiState.userPosition.longitude
                                )
                            )
                            mapViewModel.updateMovingCamera(
                                MovingCameraWrapper.MOVING(Location("UserPosition").apply {
                                    latitude = mapUiState.userPosition.latitude
                                    longitude = mapUiState.userPosition.longitude
                                })
                            )
                        }
                    }
                }
            )

            RefreshIcon(
                visibility = mapUiState.mapStatus == MapStatus.DEFAULT && refershIconVisiblity.value,
                yOffset = topBarHeight,
                onClick = {
                    refershIconVisiblity.value = false
                    MapViewModel.initialMarkerLoadFlag = false
                    mapViewModel.initialLodingStatus.value = false
                    mapViewModel.mapFiltering(appState.cameraPositionState.position.target)
                    mapViewModel.mapScrollInfoPaging(appState.cameraPositionState.position.target)
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
                        updateCategoryTags = {
                            mapViewModel.updateCategoryTags(
                                categoryTag = it,
                                position = appState.cameraPositionState.position.target
                            )
                        },
                        updateIsBookmarked = {
                            mapViewModel.updateIsBookmarked(
                                it,
                                appState.cameraPositionState.position.target
                            )
                        },
                        onSearch = {
                            mapViewModel.saveTempDatas()
                            mapViewModel.updateMapStatus(MapStatus.SEARCH_TAB)
                        }
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
                    setMapStatusDefault = {
                        mapViewModel.loadTempDatas()
                        setMapStatusDefault()
                    },
                    setMapStatusOnSearch = setMapStatusOnSearch,
                    bottomSheetContent = mapUiState.bottomSheetContents
                )
            }

            if (mapViewModel.initialLodingStatus.value) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Primary,
                    strokeWidth = 4.dp,
                )
            }

        }
    }

    if (!mapViewModel.onDetail.value.isDefault()) {
        DetailScreen(
            appState = appState,
            idx = mapViewModel.onDetail.value.id,
            storeName = mapViewModel.onDetail.value.storeName,
            onBackPress = {
                appState.bottomBarState.value = !mapUiState.mapStatus.onSearch()
                systemUiController.setSystemBarsColor(color = Color.White)
                mapViewModel.onDetail.value = DetailState.default()
            }
        )
    }


}

@Composable
private fun ManageMapStatus(
    mapStatus: MapStatus,
    systemUiController: SystemUiController,
    onSearching: MutableState<Boolean>,
    peekHeight: MutableState<Dp>,
    changeBottomBarVisibility: (Boolean) -> Unit,
    expandBottomSheet: () -> Unit,
    collapsBottomSheet: () -> Unit,
) {
    LaunchedEffect(key1 = mapStatus) {
        systemUiController.setNavigationBarColor(Color.White)
        when (mapStatus) {
            /** 기본 상태 */
            MapStatus.DEFAULT -> {
                onSearching.value = false
                systemUiController.setSystemBarsColor(Color.White)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
                changeBottomBarVisibility(true)
                collapsBottomSheet()
            }
            /** 한번 클릭했을 때 */
            MapStatus.ZOOM -> {
                onSearching.value = false
                systemUiController.setSystemBarsColor(Color.Transparent)
                systemUiController.setNavigationBarColor(Color.Transparent)
                peekHeight.value = 0.dp
                changeBottomBarVisibility(false)
                collapsBottomSheet()
            }
            /** 검색 탭에 들어갔을 때 */
            MapStatus.SEARCH_TAB -> {
                onSearching.value = true
                collapsBottomSheet()
                changeBottomBarVisibility(false)
                peekHeight.value = 0.dp
            }
            /** 지역 클릭 */
            MapStatus.LOCATION_SEARCH -> {
                onSearching.value = false
                changeBottomBarVisibility(false)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
            /** 매장 클릭 */
            MapStatus.SHOP_SEARCH -> {
                onSearching.value = false
                changeBottomBarVisibility(false)
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
            MapStatus.SEARCH_ZOOM -> {
                systemUiController.setNavigationBarColor(Color.Transparent)
                peekHeight.value = 0.dp
                changeBottomBarVisibility(false)
                collapsBottomSheet()
            }
            /** 장소 검색에서 마커 클릭 */
            MapStatus.LOCATION_SEARCH_MARKER_CLICKED -> {
                expandBottomSheet()
                peekHeight.value = BOTTOM_NAVIGATION_HEIGHT + 100.dp
            }
            /** 마커 클릭 */
            MapStatus.MARKER_CLICKED -> {
                expandBottomSheet()
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
    bottomSheetContent: BottomSheetContent,
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
                text = when (bottomSheetContent) {
                    is BottomSheetContent.MULTI -> bottomSheetContent.locationName
                    is BottomSheetContent.SINGLE -> bottomSheetContent.storeName
                    else -> ""
                },
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
    updateRefershIconVisiblity: (Boolean) -> Unit,
    expandBottomSheet: () -> Unit,
    onMapLoaded: () -> Unit,
) {

    /** 카메라 포지션 변경시 현재위치에서 다시검색 노출 */
    LaunchedEffect(key1 = cameraPositionState.position) {
        updateRefershIconVisiblity(true)
    }

    /** movingCameara에 따라 카메라 포지션 움직이기 */
    LaunchedEffect(key1 = uiState.movingCameraPosition) {
        when (uiState.movingCameraPosition) {
            MovingCameraWrapper.DEFAULT -> {
                // Do Nothing
            }
            is MovingCameraWrapper.MOVING -> {
                cameraPositionState.animate(
                    update = CameraUpdate.scrollTo(LatLng(uiState.movingCameraPosition.location))
                )
                mapViewModel.updateMovingCamera(MovingCameraWrapper.DEFAULT)
            }
        }
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
        onMapLoaded = {
            onMapLoaded()
        }
    ) {
        uiState.markerItems.forEach {
            Marker(
                state = MarkerState(position = it.position),
                icon =
                if (it.isClicked) {
                    if (it.bookmark) {
                        OverlayImage.fromResource(R.mipmap.ic_map_selceted_bookmark_marker)
                    } else {
                        OverlayImage.fromResource(R.mipmap.ic_map_selceted_marker)
                    }
                } else {
                    if (it.bookmark) {
                        OverlayImage.fromResource(R.mipmap.ic_fill_map_marker_bookmarked)
                    } else {
                        OverlayImage.fromResource(R.mipmap.ic_fill_map_marker_default)
                    }
                },
                captionText = it.title,
                height = if (it.isClicked) 61.dp else 24.dp,
                width = if (it.isClicked) 52.dp else 24.dp,
                onClick = { _ ->
                    onMarkerClick(it)
                    true
                }
            )
        }

        if (uiState.userPosition != DEFAULT_LATLNG) {
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
                mapViewModel.searchStoreId(it.storeName, it.storeId)
                mapViewModel.addRecentStr(
                    it.storeName,
                    SearchType(it.storeId, SearchType.STORE_TYPE)
                )
                expandBottomSheet()
            },
            onBackPrseed = {
                mapViewModel.loadTempDatas()
                mapViewModel.updateSearchText(TextFieldValue(""))
                onSearching.value = false
                mapViewModel.updateMapStatus(MapStatus.DEFAULT)
            },
            onLocationSearch = {
                mapViewModel.updateMapStatus(MapStatus.LOCATION_SEARCH)
                mapViewModel.searchLocationId(it.regionId)
                mapViewModel.searchLocationInfoPaging(it.region, it.regionId)
                mapViewModel.addRecentStr(
                    it.region,
                    SearchType(it.regionId, SearchType.LOCATION_TYPE)
                )
            },
            mapViewModel = mapViewModel
        )
    }
}

