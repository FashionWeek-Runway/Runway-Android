package com.cmc12th.runway.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.cmc12th.runway.data.response.home.HomeBannerItem
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.data.response.user.PatchCategoryBody
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.HomeRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.home.model.HomeBannertype
import com.cmc12th.runway.ui.signin.SignInCategoryUiState
import com.cmc12th.runway.ui.signin.model.CategoryTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val homeBanners: MutableList<HomeBannertype> = mutableListOf(),
    val nickName: String = "",
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val storeRepository: StoreRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {


    private val _homeBanners = MutableStateFlow(mutableListOf<HomeBannerItem>())
    private val _nickName = MutableStateFlow("")
    private val _reviews = MutableStateFlow<PagingData<HomeReviewItem>>(PagingData.empty())
    private val _categoryTags = MutableStateFlow(RunwayCategory.generateCategoryTags())

    val reviews: StateFlow<PagingData<HomeReviewItem>> = _reviews.asStateFlow()

    val allStores = mutableStateListOf<HomeBannerItem>()

    val uiState = combine(
        _homeBanners, _nickName
    ) { homeBannerItems, nickName ->
        val homeBanners: MutableList<HomeBannertype> = homeBannerItems.map {
            HomeBannertype.toStoreBanner(it)
        }.toMutableList()
        homeBanners.add(HomeBannertype.SHOWMOREBANNER)

        HomeUiState(
            homeBanners = homeBanners,
            nickName = nickName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    val categoryUiState: StateFlow<SignInCategoryUiState> =
        combine(_categoryTags) { categoryTags ->
            SignInCategoryUiState(
                categoryTags = categoryTags[0],
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInCategoryUiState()
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
        if (type == 0) {
            homeRepository.getHomeBanner(type).collect { apiState ->
                apiState.onSuccess {
                    _homeBanners.value = it.result
                }
            }
        } else if (type == 1) {
            homeRepository.getHomeBanner(type).collect { apiState ->
                apiState.onSuccess {
                    allStores.addAll(it.result)
                }
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

    fun getCategorys() = viewModelScope.launch {
        homeRepository.getCategories().collect { apiState ->
            apiState.onSuccess { res ->
                _categoryTags.update { items ->
                    val updatedList = items.map {
                        it.copy(
                            isSelected = res.result.contains(it.name)
                        )
                    }.toMutableList()
                    updatedList
                }
            }
        }
    }

    fun setCategorys(onSuccess: () -> Unit) = viewModelScope.launch {
        homeRepository.setCategories(
            PatchCategoryBody(
                _categoryTags.value.filter { it.isSelected }.map { it.id }
            )
        ).collect { apiState ->
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
        for (i in 0 until allStores.size) {
            if (allStores[i].storeId == storedId) {
                allStores[i] = allStores[i].copy(bookmark = bookmarked)
            }
        }
    }

    fun updateCategoryTags(categoryTag: CategoryTag) {
        _categoryTags.value = _categoryTags.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
    }


}