package com.cmc12th.runway.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.ui.map.model.NaverItem
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.data.response.map.toNaverMapItem
import com.cmc12th.runway.domain.repository.MapRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LOCATION
import com.cmc12th.runway.ui.map.model.BottomSheetContent
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class MapUiState(
    val markerItems: List<NaverItem> = emptyList(),
    val categoryItems: List<CategoryTag> = emptyList(),
    val isBookmarked: Boolean = false,
    val userPosition: LatLng = DEFAULT_LATLNG,
    val movingCameraPosition: Location = DEFAULT_LOCATION,
    val bottomSheetContents: BottomSheetContent = BottomSheetContent.DEFAULT,
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storeRepository: StoreRepository,
    private val mapRepository: MapRepository,
) : ViewModel(), LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(5000) // 초기 1회만 가져오고 Long.MAX_VALUE 만큼 기다림
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    private val locationCallback: CustomLocationCallback = CustomLocationCallback()

    // 맵에 찍히는 마커 아이템
    private val _markerItems: MutableStateFlow<List<NaverItem>> = MutableStateFlow(emptyList())

    // 바텀 시트 아이템
    private val _bottomsheetItem: MutableStateFlow<BottomSheetContent> =
        MutableStateFlow(BottomSheetContent.DEFAULT)

    // 검색 탭 및 필터링 되는 아이템들
    private val _categoryItems: MutableStateFlow<List<CategoryTag>> =
        MutableStateFlow(RunwayCategory.generateCategoryTags())
    private val _isBookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /** 해당 값은 변할 시 카메라가 이동함 */
    private val _movingCameraPosition = MutableStateFlow(DEFAULT_LOCATION)

    private val _userPosition = MutableStateFlow(DEFAULT_LATLNG)

    val mapUiState = combine(
        _markerItems,
        _categoryItems,
        _isBookmarked,
        _userPosition,
        _movingCameraPosition,
        _bottomsheetItem,
    ) { resultArr ->
        @Suppress("UNCHECKED_CAST")
        MapUiState(
            markerItems = resultArr[0] as List<NaverItem>,
            categoryItems = resultArr[1] as List<CategoryTag>,
            isBookmarked = resultArr[2] as Boolean,
            userPosition = resultArr[3] as LatLng,
            movingCameraPosition = resultArr[4] as Location,
            bottomSheetContents = resultArr[5] as BottomSheetContent
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MapUiState()
    )


    @SuppressLint("MissingPermission")
    fun addLocationListener() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    /** 맵 스크롤 정보(여러개) 가져오기 */
    fun mapScrollInfo(latLng: LatLng) = viewModelScope.launch {
        mapRepository.mapInfoPaging(page = 0, size = 10,
            mapFilterRequest = MapFilterRequest(
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                category = _categoryItems.value.filter { it.isSelected }.map { it.name },
            )
        ).collect { apiState ->
            apiState.onSuccess {
                _bottomsheetItem.value = BottomSheetContent.MULTI(it.pagingMetadata.contents)
            }
            apiState.onError {

            }
        }
    }

    fun mapFiltering(latLng: LatLng) = viewModelScope.launch {
        mapRepository.mapFiltering(
            MapFilterRequest(
                category = _categoryItems.value.filter { it.isSelected }.map { it.name },
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
        ).collect { apiState ->
            apiState.onSuccess {
                updateMarkerItems(it.result.toNaverMapItem())
            }
            apiState.onError {

            }
        }
    }

    /** 단일 맵 정보 가져오기 */
    fun mapInfo(storeId: Int) = viewModelScope.launch {
        updateBottomSheetItem(BottomSheetContent.LOADING)
        mapRepository.mapInfo(storeId).collect { apiState ->
            apiState.onSuccess {
                updateBottomSheetItem(BottomSheetContent.SINGLE(it.result))
            }
            apiState.onError {
                updateBottomSheetItem(BottomSheetContent.DEFAULT)
            }
        }
    }

    fun removeLocationListener() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun updateIsBookmarked(isBookmarked: Boolean) {
        _isBookmarked.value = isBookmarked
    }

    fun updateCategoryTags(categoryTag: CategoryTag) {
        _categoryItems.value = _categoryItems.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
    }

    private fun updateMarkerItems(naverItems: List<NaverItem>) {
        _markerItems.value = naverItems
    }

    fun updateMarker(copy: NaverItem) {
        // TODO 로직 수정 필요
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
        // TODO 로직 수정 필요
        val temp = mutableListOf<NaverItem>()
        _markerItems.value.forEach {
            temp.add(it.copy(isClicked = false))
        }
        _markerItems.value = temp
    }

    private fun updateBottomSheetItem(bottomsheetItem: BottomSheetContent) {
        _bottomsheetItem.value = bottomsheetItem
    }

    private val _testMarkers = mutableStateOf(NaverItem.default())
    val testMarkers: State<NaverItem> get() = _testMarkers
    fun setTestMarker(naverItem: NaverItem) {
        _testMarkers.value = naverItem
    }

    var initialMarkerLoadFlag = true

    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                // 초기 1회 진입할 때 마커 불러오기
                if (initialMarkerLoadFlag) {
                    mapFiltering(LatLng(it.latitude, it.longitude))
                    mapScrollInfo(LatLng(it.latitude, it.longitude))
                    initialMarkerLoadFlag = false
//                    _movingCameraPosition.value = it
                }
                _userPosition.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    companion object {
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

        val DEFAULT_LOCATION = Location("성수").apply {
            latitude = 37.5437
            longitude = 127.0659
        }
        val DEFAULT_LATLNG = LatLng(37.5437, 127.0659)
    }
}