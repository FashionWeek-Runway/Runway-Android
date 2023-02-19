package com.cmc12th.runway.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.domain.repository.MapRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
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
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storeRepository: StoreRepository,
    private val mapRepository: MapRepository
) : ViewModel(), LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(5000) // 초기 1회만 가져오고 Long.MAX_VALUE 만큼 기다림
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    private val locationCallback: CustomLocationCallback = CustomLocationCallback()

    // 맵에 찍히는 마커 아이템
    private val _markerItems: MutableStateFlow<List<NaverItem>> = MutableStateFlow(DUMMY_NAVER_ITEM)

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
    ) { markerItems, categoryItems, isBookmarked, userPosition ->
        MapUiState(
            markerItems = markerItems,
            categoryItems = categoryItems,
            isBookmarked = isBookmarked,
            userPosition = userPosition,
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

    fun mapFiltering(latLng: LatLng) = viewModelScope.launch {
        mapRepository.mapFiltering(
            MapFilterRequest(
                category = _categoryItems.value.filter { it.isSelected }.map { it.name },
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
        ).collect { apiState ->
            apiState.onSuccess {
                it.result
            }
            apiState.onError {

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

    fun stores() = viewModelScope.launch {
        storeRepository.store().collect {
            Log.i("dlgocks1", it.toString())
        }
    }

    fun setMarkerItems(naverItems: List<NaverItem>) {
        _markerItems.value = naverItems
    }

    inner class CustomLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                _userPosition.value = LatLng(it.latitude, it.longitude)
                _movingCameraPosition.value = it
            }
        }
    }

    companion object {
        val DUMMY_NAVER_ITEM = listOf<NaverItem>(
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

        private val DEFAULT_LOCATION = Location("성수").apply {
            latitude = 37.5437
            longitude = 127.0659
        }
        val DEFAULT_LATLNG = LatLng(37.5437, 127.0659)
    }
}