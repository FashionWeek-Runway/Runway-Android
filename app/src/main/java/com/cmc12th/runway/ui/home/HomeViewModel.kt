package com.cmc12th.runway.ui.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.room.PrimaryKey
import com.cmc12th.runway.data.response.home.HomeBannerItem
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.HomeRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.ui.setting.SettingPersonalInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class homeUiState(
    val homeBanners: MutableList<HomeBannerItem> = mutableListOf(),
    val nickName: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val storeRepository: StoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _homeBanners = MutableStateFlow(mutableListOf<HomeBannerItem>())
    private val _nickName = MutableStateFlow("")
    private val _reviews = MutableStateFlow<PagingData<HomeReviewItem>>(PagingData.empty())
    val reviews: StateFlow<PagingData<HomeReviewItem>> = _reviews.asStateFlow()

    val uiState = combine(
        _homeBanners, _nickName
    ) { homeBannerItems, nickName ->
        homeUiState(
            homeBanners = homeBannerItems,
            nickName = nickName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = homeUiState()
    )

    fun getHomeReview() = viewModelScope.launch {
        homeRepository.getHomeReviewPaging().collect {
            _reviews.value = it
        }
    }

    fun getProfile() = viewModelScope.launch {
        authRepository.getProfileInfoToEdit().collect { apiState ->
            apiState.onSuccess {
                _nickName.value = it.result.nickname
            }
        }
    }

    fun getHomeBanner(type: Int) = viewModelScope.launch {
        homeRepository.getHomeBanner(type).collect { apiState ->
            apiState.onSuccess {
                _homeBanners.value = it.result
            }
        }
    }

    fun updateBookmark(storeId: Int, onSuccess: () -> Unit) = viewModelScope.launch {
        storeRepository.storeBookmark(storeId = storeId).collect { apiState ->
            apiState.onSuccess {
                onSuccess()
            }
        }
    }

    fun updateBookmarkState(storedId: Int, bookmarked: Boolean) {
        _homeBanners.update { items ->
            val updatedList = items.map {
                it.copy(
                    bookmark = if (it.storeId == storedId) bookmarked else it.bookmark
                )
            }.toMutableList()
            updatedList
        }
    }
}