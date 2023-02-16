package com.cmc12th.runway.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.MapViewModel.Companion.DEFAULT_LATLNG
import com.cmc12th.runway.ui.signin.SignInUserVerificationUiState
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.utils.Constants
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
) : ViewModel(), LifecycleObserver {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(Long.MAX_VALUE) // 초기 1회만 가져오고 Long.MAX_VALUE 만큼 기다림
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    private val myLocationCallback: MyLocationCallback = MyLocationCallback()

    // 맵에 찍히는 마커 아이템
    val _markerItems: MutableStateFlow<List<NaverItem>> = MutableStateFlow(DUMMY_NAVER_ITEM)

    // 검색 탭 및 필터링 되는 아이템들
    val _categoryItems: MutableStateFlow<List<CategoryTag>> =
        MutableStateFlow(RunwayCategory.generateCategoryTags())
    val _isBookmarked: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /** 해당 값은 변할 시 카메라가 이동함 */
    private val _movingCameraPosition = MutableStateFlow(DEFAULT_LOCATION)
    val movingCameraPosition: StateFlow<Location> get() = _movingCameraPosition

    private val _userPosition = MutableStateFlow(DEFAULT_LATLNG)
    val userPosition: StateFlow<LatLng> get() = _userPosition

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
            myLocationCallback,
            Looper.getMainLooper(),
        )
    }

    fun removeLocationListener() {
        fusedLocationClient.removeLocationUpdates(myLocationCallback)
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

    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                Log.i("dlgocks1 : User-Position", it.toString())
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