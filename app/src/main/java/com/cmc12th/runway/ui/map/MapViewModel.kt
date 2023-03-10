package com.cmc12th.runway.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.cmc12th.runway.data.model.RecentStr
import com.cmc12th.runway.data.model.SearchType
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.RegionSearch
import com.cmc12th.runway.data.response.map.StoreSearch
import com.cmc12th.runway.data.response.map.toNaverMapItem
import com.cmc12th.runway.domain.repository.MapRepository
import com.cmc12th.runway.domain.repository.SearchRepository
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.map.model.BottomSheetContent
import com.cmc12th.runway.ui.map.model.MapStatus
import com.cmc12th.runway.ui.map.model.MovingCameraWrapper
import com.cmc12th.runway.ui.map.model.NaverItem
import com.cmc12th.runway.ui.signin.model.CategoryTag
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
    val markerItems: List<NaverItem> = emptyList(),
    val categoryItems: List<CategoryTag> = emptyList(),
    val isBookmarked: Boolean = false,
    val userPosition: LatLng = DEFAULT_LATLNG,
    val movingCameraPosition: MovingCameraWrapper = MovingCameraWrapper.DEFAULT,
    val bottomSheetContents: BottomSheetContent = BottomSheetContent.DEFAULT,
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
    private val mapRepository: MapRepository,
    private val searchRepository: SearchRepository,
) : ViewModel(), LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(5000) // ?????? 1?????? ???????????? Long.MAX_VALUE ?????? ?????????
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    private val locationCallback: CustomLocationCallback = CustomLocationCallback()

    // ?????? ????????? ?????? ?????????
    private val _markerItems: MutableStateFlow<List<NaverItem>> = MutableStateFlow(emptyList())

    // ?????? ?????? ?????????
    private val _bottomsheetItem: MutableStateFlow<BottomSheetContent> =
        MutableStateFlow(BottomSheetContent.DEFAULT)

    // ?????? ??? ??? ????????? ?????? ????????????
    private val _categoryItems: MutableStateFlow<List<CategoryTag>> =
        MutableStateFlow(RunwayCategory.generateCategoryTags())
    private val _isBookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val initialLodingStatus = mutableStateOf(true)

    /** ?????? ?????? ?????? ??? ???????????? ????????? */
    private val _movingCameraPosition: MutableStateFlow<MovingCameraWrapper> =
        MutableStateFlow(MovingCameraWrapper.DEFAULT)

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
            markerItems = resultArr[0] as List<NaverItem>,
            categoryItems = resultArr[1] as List<CategoryTag>,
            isBookmarked = resultArr[2] as Boolean,
            userPosition = resultArr[3] as LatLng,
            movingCameraPosition = resultArr[4] as MovingCameraWrapper,
            bottomSheetContents = resultArr[5] as BottomSheetContent,
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


    /** ?????? ??? ?????? */
    fun mapSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200) // ???????????? 0.2??? ??????
            if (_searchText.value.text.isNotBlank()) {
                mapRepository.mapSearch(
                    MapSearchRequest(
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

    /** ??? ????????? ??????(?????????) ???????????? */
    fun mapScrollInfoPaging(latLng: LatLng) = viewModelScope.launch {
        mapRepository.getMpaInfoPagingItem(mapFilterRequest = MapFilterRequest(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            category = _categoryItems.value.filter { it.isSelected }.map { it.name },
        )
        ).cachedIn(viewModelScope).collect {
            _bottomsheetItem.value =
                BottomSheetContent.MULTI(
                    "",
                    MutableStateFlow(PagingData.empty())
                ).apply {
                    this.contents.value = if (_isBookmarked.value) it.filter { it.bookmark } else it
                }
        }
    }

    private var scrollTemp: BottomSheetContent = BottomSheetContent.DEFAULT
    private var markerItemsTemp = emptyList<NaverItem>()
    private var locationScrollTemp: BottomSheetContent = BottomSheetContent.DEFAULT

    /** ?????? ???????????? ??? ?????? ?????? ???????????? */
    fun mapInfo(storeId: Int) = viewModelScope.launch {
        updateBottomSheetItem(BottomSheetContent.LOADING)
        mapRepository.mapInfo(storeId).collect { apiState ->
            apiState.onSuccess {
                updateBottomSheetItem(BottomSheetContent.SINGLE(contents = it.result))
            }
            apiState.onError {
                updateBottomSheetItem(BottomSheetContent.DEFAULT)
            }
        }
    }

    /** ?????? ????????? ????????? ?????? ???????????? */
    fun mapFiltering(latLng: LatLng) = viewModelScope.launch {
        mapRepository.mapFiltering(
            MapFilterRequest(
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

    /** ????????? ????????? ?????? */
    fun searchStoreId(storeName: String, storeId: Int) = viewModelScope.launch {
        mapRepository.storeSearch(storeId).collect { apiState ->
            apiState.onSuccess {
                updateBottomSheetItem(
                    BottomSheetContent.SINGLE(
                        storeName,
                        it.result.storeInfo.toMapInfoItem()
                    )
                )
                updateMovingCamera(
                    MovingCameraWrapper.MOVING(
                        Location("SelectedMarker").apply {
                            latitude = it.result.mapMarker.latitude - 0.01
                            longitude = it.result.mapMarker.longitude
                        }
                    )

                )
                updateMarkerItems(it.result.mapMarker.toSingleMarkerItem())
            }
        }
    }

    /** ?????? ????????? ?????? */
    fun searchLocationId(regionId: Int) = viewModelScope.launch {
        mapRepository.locationMarkerSearch(regionId).collect { apiState ->
            apiState.onSuccess { it ->
                updateMovingCamera(
                    MovingCameraWrapper.MOVING(
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

    /** ?????? ?????? ??? ?????? ????????? ????????? */
    fun searchLocationInfoPaging(region: String, regionId: Int) = viewModelScope.launch {
        mapRepository.getLocationInfoPagingItem(
            regionId = regionId,
        ).cachedIn(viewModelScope).collect {
            _bottomsheetItem.value =
                BottomSheetContent.MULTI(region, MutableStateFlow(PagingData.empty())).apply {
                    this.contents.value = it
                }
        }
    }


    fun saveTempDatas() {
        if (_bottomsheetItem.value is BottomSheetContent.MULTI) {
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
                // ?????? ????????? ??????????????? ????????? ??? ??????
                loadTempDatas()
                resetSelectedMarkers()
            }
            MapStatus.LOCATION_SEARCH_MARKER_CLICKED -> {
                _mapStatus.value = MapStatus.LOCATION_SEARCH
                // ?????? ????????? ??????????????? ????????? ??? ??????
                loadLocationTempDatas()
                resetSelectedMarkers()
            }
            else -> {}
        }
    }

    fun addRecentStr(searchStr: String, searchType: SearchType) = viewModelScope.launch {
        val dateInfo = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("MM.dd"))
        val dbItem = _recentSearchs.value.find {
            it.value == searchStr
        }
        if (dbItem == null) {
            searchRepository.addSearchStr(RecentStr(searchStr, dateInfo, searchType))
        } else {
            searchRepository.addSearchStr(RecentStr(searchStr, dateInfo, searchType).apply {
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

    fun updateMovingCamera(movingCameraPosition: MovingCameraWrapper) {
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

    fun updateCategoryTags(categoryTag: CategoryTag, position: LatLng) {
        _categoryItems.value = _categoryItems.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
        mapFiltering(position)
        mapScrollInfoPaging(position)
    }

    private fun updateMarkerItems(naverItems: List<NaverItem>) {
        _markerItems.value = naverItems
    }

    fun updateMarker(copy: NaverItem) {
        // TODO ?????? ?????? ??????
        val temp = mutableListOf<NaverItem>()
        _markerItems.value.forEach {
            if (it.storeId == copy.storeId) {
                temp.add(it.copy(isClicked = copy.isClicked))
            } else {
                temp.add(it.copy(isClicked = false))
            }
        }
        _markerItems.value = temp
    }

    fun resetSelectedMarkers() {
        // TODO ?????? ?????? ??????
        val temp = mutableListOf<NaverItem>()
        _markerItems.value.forEach {
            temp.add(it.copy(isClicked = false))
        }
        _markerItems.value = temp
    }


    private fun updateBottomSheetItem(bottomsheetItem: BottomSheetContent) {
        _bottomsheetItem.value = bottomsheetItem
    }

    fun updateSearchText(searchText: TextFieldValue) {
        _searchText.value = searchText
    }


    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            locationResult.lastLocation?.let {
                // ?????? 1??? ????????? ??? ?????? ????????????
                if (initialMarkerLoadFlag) {
                    initialMarkerLoadFlag = false
                    initialLodingStatus.value = false
                    mapFiltering(LatLng(it.latitude, it.longitude))
                    mapScrollInfoPaging(LatLng(it.latitude, it.longitude))
                    _movingCameraPosition.value = MovingCameraWrapper.MOVING(it)
                }
                _userPosition.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    companion object {
        var initialMarkerLoadFlag = true

        val DUMMY_NAVER_ITEM = listOf<NaverItem>(
//            NaverItem(37.540791, 127.096306),
//            NaverItem(37.550791, 127.076306),
//            NaverItem(37.560791, 127.066306),
//            NaverItem(37.550791, 127.166306),
//            NaverItem(37.540791, 127.176306),
//            NaverItem(37.520791, 127.166306),
//            NaverItem(37.510791, 127.016306),
//            NaverItem(37.570791, 127.046306),
        )

        val DEFAULT_LOCATION = Location("??????").apply {
            latitude = 37.5437
            longitude = 127.0659
        }
        val DEFAULT_LATLNG = LatLng(37.5437, 127.0659)
    }
}