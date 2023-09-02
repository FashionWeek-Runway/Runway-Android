package com.cmc12th.runway.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.cmc12th.domain.model.RecentStr
import com.cmc12th.domain.model.SearchType
import com.cmc12th.domain.model.map.model.MapStatus
import com.cmc12th.domain.model.response.map.RegionSearch
import com.cmc12th.domain.model.response.map.StoreSearch
import com.cmc12th.domain.model.response.map.toNaverMapItem
import com.cmc12th.domain.model.signin.CategoryTag
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
import com.cmc12th.runway.ui.map.components.DetailState
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Stable
data class MapUiState(
    val markerItems: List<com.cmc12th.domain.model.map.model.NaverItem> = emptyList(),
    val categoryItems: List<CategoryTag> = emptyList(),
    val isBookmarked: Boolean = false,
    val userPosition: LatLng = DEFAULT_LATLNG,
    val movingCameraPosition: com.cmc12th.domain.model.map.model.MovingCameraWrapper = com.cmc12th.domain.model.map.model.MovingCameraWrapper.DEFAULT,
    val bottomSheetContents: com.cmc12th.domain.model.map.model.BottomSheetContent = com.cmc12th.domain.model.map.model.BottomSheetContent.DEFAULT,
    val mapStatus: MapStatus = MapStatus.DEFAULT,
)

@Stable
data class SearchUiState(
    val searchText: TextFieldValue = TextFieldValue(""),
    val regionSearchs: List<RegionSearch> = emptyList(),
    val storeSearchs: List<StoreSearch> = emptyList(),
    val recentSearchs: List<RecentStr> = emptyList(),
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mapRepository: com.cmc12th.domain.repository.MapRepository,
    private val searchRepository: com.cmc12th.domain.repository.SearchRepository,
) : ViewModel(), LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(2000) // 초기 1회만 가져오고 Long.MAX_VALUE 만큼 기다림
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    private val locationCallback: CustomLocationCallback = CustomLocationCallback()

    // 맵에 찍히는 마커 아이템
    private val _markerItems: MutableStateFlow<List<com.cmc12th.domain.model.map.model.NaverItem>> =
        MutableStateFlow(emptyList())

    // 바텀 시트 아이템
    private val _bottomsheetItem: MutableStateFlow<com.cmc12th.domain.model.map.model.BottomSheetContent> =
        MutableStateFlow(com.cmc12th.domain.model.map.model.BottomSheetContent.DEFAULT)

    // 검색 탭 및 필터링 되는 아이템들
    private val _categoryItems: MutableStateFlow<List<CategoryTag>> =
        MutableStateFlow(RunwayCategory.generateCategoryTags())
    private val _isBookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val initialLodingStatus = mutableStateOf(true)

    /** 해당 값은 변할 시 카메라가 이동함 */
    private val _movingCameraPosition: MutableStateFlow<com.cmc12th.domain.model.map.model.MovingCameraWrapper> =
        MutableStateFlow(com.cmc12th.domain.model.map.model.MovingCameraWrapper.DEFAULT)

    private val _mapStatus: MutableStateFlow<MapStatus> =
        MutableStateFlow<MapStatus>(MapStatus.DEFAULT)

    private val _userPosition = MutableStateFlow(DEFAULT_LATLNG)

    val onDetail = mutableStateOf(DetailState.default())

    val mapUiState = combine(
        _markerItems,
        _categoryItems,
        _isBookmarked,
        _userPosition,
        _movingCameraPosition,
        _bottomsheetItem,
        _mapStatus,
    ) { resultArr ->
        @Suppress("UNCHECKED_CAST")
        MapUiState(
            markerItems = resultArr[0] as List<com.cmc12th.domain.model.map.model.NaverItem>,
            categoryItems = resultArr[1] as List<CategoryTag>,
            isBookmarked = resultArr[2] as Boolean,
            userPosition = resultArr[3] as LatLng,
            movingCameraPosition = resultArr[4] as com.cmc12th.domain.model.map.model.MovingCameraWrapper,
            bottomSheetContents = resultArr[5] as com.cmc12th.domain.model.map.model.BottomSheetContent,
            mapStatus = resultArr[6] as MapStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MapUiState()
    )

    private val _searchText = MutableStateFlow<TextFieldValue>(TextFieldValue(""))
    private val _regionSearch = MutableStateFlow(listOf<RegionSearch>())
    private val _storeSearch = MutableStateFlow(listOf<StoreSearch>())

    private val _recentSearchs = MutableStateFlow(emptyList<RecentStr>())

    init {
        viewModelScope.launch {
            searchRepository.getRecentSearchAll().collectLatest { recentList ->
                Log.i("dlgocks1-recentSearch", recentList.toString())
                _recentSearchs.value = recentList
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    val searchUiState = combine(
        _searchText,
        _regionSearch,
        _storeSearch,
        _recentSearchs
    ) { flowArr ->
        SearchUiState(
            searchText = flowArr[0] as TextFieldValue,
            regionSearchs = flowArr[1] as List<RegionSearch>,
            storeSearchs = flowArr[2] as List<StoreSearch>,
            recentSearchs = flowArr[3] as List<RecentStr>
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    @SuppressLint("MissingPermission")
    fun addLocationListener() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    private var searchJob: Job? = null


    /** 검색 탭 검색 */
    fun mapSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200) // 디바운싱 0.2초 적용
            if (_searchText.value.text.isNotBlank()) {
                mapRepository.mapSearch(
                    com.cmc12th.domain.model.request.map.MapSearchRequest(
                        content = _searchText.value.text,
                        latitude = _userPosition.value.latitude,
                        longitude = _userPosition.value.longitude
                    )
                ).collect { apiState ->
                    apiState.onSuccess {
                        _storeSearch.value = it.result.storeSearchList
                        _regionSearch.value = it.result.regionSearchList
                    }
                }
            }
        }
    }

    /** 맵 스크롤 정보(여러개) 가져오기 */
    fun mapScrollInfoPaging(latLng: LatLng) = viewModelScope.launch {
        mapRepository.getMpaInfoPagingItem(mapFilterRequest = com.cmc12th.domain.model.request.map.MapFilterRequest(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            category = _categoryItems.value.filter { it.isSelected }.map { it.name },
        )
        ).cachedIn(viewModelScope).collect {
            _bottomsheetItem.value =
                com.cmc12th.domain.model.map.model.BottomSheetContent.MULTI(
                    "",
                    MutableStateFlow(PagingData.empty())
                ).apply {
                    this.contents.value = if (_isBookmarked.value) it.filter { it.bookmark } else it
                }
        }
    }

    private var scrollTemp: com.cmc12th.domain.model.map.model.BottomSheetContent =
        com.cmc12th.domain.model.map.model.BottomSheetContent.DEFAULT
    private var markerItemsTemp = emptyList<com.cmc12th.domain.model.map.model.NaverItem>()
    private var locationScrollTemp: com.cmc12th.domain.model.map.model.BottomSheetContent =
        com.cmc12th.domain.model.map.model.BottomSheetContent.DEFAULT

    /** 마커 클릭했을 때 하단 정보 가져오기 */
    fun mapInfo(storeId: Int) = viewModelScope.launch {
        updateBottomSheetItem(com.cmc12th.domain.model.map.model.BottomSheetContent.LOADING)
        mapRepository.mapInfo(storeId).collect { apiState ->
            apiState.onSuccess {
                updateBottomSheetItem(
                    com.cmc12th.domain.model.map.model.BottomSheetContent.SINGLE(
                        contents = it.result
                    )
                )
            }
            apiState.onError {
                updateBottomSheetItem(com.cmc12th.domain.model.map.model.BottomSheetContent.DEFAULT)
            }
        }
    }

    /** 맵에 보이는 마커들 정보 가져오기 */
    fun mapFiltering(latLng: LatLng) = viewModelScope.launch {
        mapRepository.mapFiltering(
            com.cmc12th.domain.model.request.map.MapFilterRequest(
                category = _categoryItems.value.filter { it.isSelected }.map { it.name },
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
        ).collect { apiState ->
            apiState.onSuccess { responseWrapper ->
                if (_isBookmarked.value) {
                    updateMarkerItems(
                        responseWrapper.result.toNaverMapItem().filter {
                            it.bookmark
                        }
                    )
                } else {
                    updateMarkerItems(
                        responseWrapper.result.toNaverMapItem()
                    )
                }
            }
            apiState.onError {

            }
        }
    }

    /** 스토어 아이디 검색 */
    fun searchStoreId(storeName: String, storeId: Int) = viewModelScope.launch {
        mapRepository.storeSearch(storeId).collect { apiState ->
            apiState.onSuccess {
                updateBottomSheetItem(
                    com.cmc12th.domain.model.map.model.BottomSheetContent.SINGLE(
                        storeName,
                        it.result.storeInfo.toMapInfoItem()
                    )
                )
                updateMovingCamera(
                    com.cmc12th.domain.model.map.model.MovingCameraWrapper.MOVING(
                        Location("SelectedMarker").apply {
                            latitude = it.result.mapMarker.latitude
                            longitude = it.result.mapMarker.longitude
                        }
                    )

                )
                updateMarkerItems(it.result.mapMarker.toSingleMarkerItem())
            }
        }
    }

    /** 장소 아이디 검색 */
    fun searchLocationId(regionId: Int) = viewModelScope.launch {
        mapRepository.locationMarkerSearch(regionId).collect { apiState ->
            apiState.onSuccess { it ->
                updateMovingCamera(
                    com.cmc12th.domain.model.map.model.MovingCameraWrapper.MOVING(
                        Location("AveragePosition").apply {
                            latitude = it.result.map { it.latitude }.average()
                            longitude = it.result.map { it.longitude }.average()
                        }
                    )
                )
                updateMarkerItems(it.result.map {
                    it.toMarkerItem()
                })
            }
        }
    }

    /** 장소 검색 시 바텀 스크롤 아이템 */
    fun searchLocationInfoPaging(region: String, regionId: Int) = viewModelScope.launch {
        mapRepository.getLocationInfoPagingItem(
            regionId = regionId,
        ).cachedIn(viewModelScope).collect {
            _bottomsheetItem.value =
                com.cmc12th.domain.model.map.model.BottomSheetContent.MULTI(
                    region,
                    MutableStateFlow(PagingData.empty())
                ).apply {
                    this.contents.value = it
                }
        }
    }


    fun saveTempDatas() {
        if (_bottomsheetItem.value is com.cmc12th.domain.model.map.model.BottomSheetContent.MULTI) {
            scrollTemp = _bottomsheetItem.value
            markerItemsTemp = _markerItems.value
        }
    }

    fun saveLocationTempDatas() {
        locationScrollTemp = _bottomsheetItem.value
    }

    fun loadTempDatas() {
        _bottomsheetItem.value = scrollTemp
        _markerItems.value = markerItemsTemp
    }

    fun loadLocationTempDatas() {
        _bottomsheetItem.value = locationScrollTemp
    }

    fun onMapClick() {
        when (_mapStatus.value) {
            MapStatus.DEFAULT -> {
                resetSelectedMarkers()
                _mapStatus.value = MapStatus.ZOOM
            }

            MapStatus.ZOOM -> {
                _mapStatus.value = MapStatus.DEFAULT
            }

            MapStatus.LOCATION_SEARCH -> {
            }

            MapStatus.SHOP_SEARCH -> {
                _mapStatus.value = MapStatus.SEARCH_ZOOM
            }

            MapStatus.SEARCH_ZOOM -> {
                _mapStatus.value = MapStatus.SHOP_SEARCH
            }

            MapStatus.MARKER_CLICKED -> {
                _mapStatus.value = MapStatus.DEFAULT
                // 단일 항목을 클릭했다가 돌아올 때 복구
                loadTempDatas()
                resetSelectedMarkers()
            }

            MapStatus.LOCATION_SEARCH_MARKER_CLICKED -> {
                _mapStatus.value = MapStatus.LOCATION_SEARCH
                // 단일 항목을 클릭했다가 돌아올 때 복구
                loadLocationTempDatas()
                resetSelectedMarkers()
            }

            else -> {}
        }
    }

    fun addRecentStr(searchStr: String, searchType: SearchType) =
        viewModelScope.launch {
            val dateInfo = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM.dd"))
            val dbItem = _recentSearchs.value.find {
                it.value == searchStr
            }
            if (dbItem == null) {
                searchRepository.addSearchStr(
                    RecentStr(
                        searchStr,
                        dateInfo,
                        searchType
                    )
                )
            } else {
                searchRepository.addSearchStr(
                    RecentStr(
                        searchStr,
                        dateInfo,
                        searchType
                    ).apply {
                        id = dbItem.id
                    })
            }
        }

    fun removeRecentStr(id: Int) = viewModelScope.launch {
        _recentSearchs.value.find {
            it.id == id
        }?.let {
            searchRepository.removeSearchStr(it)
        }
    }

    fun removeAllRecentStr() = viewModelScope.launch {
        searchRepository.removeAllSearchStr()
    }

    fun updateMovingCamera(movingCameraPosition: com.cmc12th.domain.model.map.model.MovingCameraWrapper) {
        _movingCameraPosition.value = movingCameraPosition
    }

    fun updateMapStatus(mapStatus: MapStatus) {
        _mapStatus.value = mapStatus
    }

    fun removeLocationListener() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun updateIsBookmarked(isBookmarked: Boolean, position: LatLng) {
        _isBookmarked.value = isBookmarked
        mapFiltering(position)
        mapScrollInfoPaging(position)
    }

    fun updateCategoryTags(
        categoryTag: CategoryTag,
        position: LatLng
    ) {
        _categoryItems.value = _categoryItems.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
        mapFiltering(position)
        mapScrollInfoPaging(position)
    }

    private fun updateMarkerItems(naverItems: List<com.cmc12th.domain.model.map.model.NaverItem>) {
        _markerItems.value = naverItems
    }

    fun updateMarker(copy: com.cmc12th.domain.model.map.model.NaverItem) {
        // TODO 로직 수정 필요
        val temp = mutableListOf<com.cmc12th.domain.model.map.model.NaverItem>()
        _markerItems.value.forEach {
            if (it.storeId == copy.storeId) {
                temp.add(it.copy(isClicked = copy.isClicked))
            } else {
                temp.add(it.copy(isClicked = false))
            }
        }
        _markerItems.value = temp
    }

    private fun resetSelectedMarkers() {
        // TODO 로직 수정 필요
        val temp = mutableListOf<com.cmc12th.domain.model.map.model.NaverItem>()
        _markerItems.value.forEach {
            temp.add(it.copy(isClicked = false))
        }
        _markerItems.value = temp
    }


    private fun updateBottomSheetItem(bottomsheetItem: com.cmc12th.domain.model.map.model.BottomSheetContent) {
        _bottomsheetItem.value = bottomsheetItem
    }

    fun updateSearchText(searchText: TextFieldValue) {
        _searchText.value = searchText
    }


    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            locationResult.lastLocation?.let {
                // 초기 1회 진입할 때 마커 불러오기
                if (initialMarkerLoadFlag) {
                    initialMarkerLoadFlag = false
                    initialLodingStatus.value = false
                    mapFiltering(LatLng(it.latitude, it.longitude))
                    mapScrollInfoPaging(LatLng(it.latitude, it.longitude))
                    _movingCameraPosition.value =
                        com.cmc12th.domain.model.map.model.MovingCameraWrapper.MOVING(it)
                }
                _userPosition.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    companion object {
        var initialMarkerLoadFlag = true

        val DUMMY_NAVER_ITEM = listOf<com.cmc12th.domain.model.map.model.NaverItem>(
//            NaverItem(37.540791, 127.096306),
//            NaverItem(37.550791, 127.076306),
//            NaverItem(37.560791, 127.066306),
//            NaverItem(37.550791, 127.166306),
//            NaverItem(37.540791, 127.176306),
//            NaverItem(37.520791, 127.166306),
//            NaverItem(37.510791, 127.016306),
//            NaverItem(37.570791, 127.046306),
        )

        val DEFAULT_LOCATION = Location("성수").apply {
            latitude = 37.5437
            longitude = 127.0659
        }
        val DEFAULT_LATLNG = LatLng(37.5437, 127.0659)
    }
}