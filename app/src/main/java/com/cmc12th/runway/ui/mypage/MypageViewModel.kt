package com.cmc12th.runway.ui.mypage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.ui.login.passwordsearch.SearchPasswordPhoneUiState
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MypageUiState(
    val onDetail: DetailState = DetailState.default(),
    val selectedPage: MypageTabInfo = MypageTabInfo.MY_REVIEW
)

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _myReviews = MutableStateFlow<PagingData<MyReviewsItem>>(PagingData.empty())
    val myReviews: StateFlow<PagingData<MyReviewsItem>> = _myReviews.asStateFlow()
    private val _bookmarkedStore =
        MutableStateFlow<PagingData<StoreMetaDataItem>>(PagingData.empty())
    val bookmarkedStore: StateFlow<PagingData<StoreMetaDataItem>> = _bookmarkedStore.asStateFlow()
    private val _onDetail = MutableStateFlow(DetailState.default())
    private val _selectedPage = MutableStateFlow(MypageTabInfo.MY_REVIEW)

    val uiState = combine(_onDetail, _selectedPage) { onDetail, selectedPage ->
        MypageUiState(
            onDetail = onDetail,
            selectedPage = selectedPage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MypageUiState()
    )


    fun getMyReviews() = viewModelScope.launch {
        authRepository.myReviewPaging().cachedIn(viewModelScope).collect {
            _myReviews.value = it
        }
    }

    fun getBookmarkedStore() = viewModelScope.launch {
        authRepository.bookmarkedStorePaging().cachedIn(viewModelScope).collect {
            _bookmarkedStore.value = it
        }
    }

    fun updateOnDetail(state: DetailState) {
        _onDetail.value = state
    }

    fun updateSelectedPage(tabInfo: MypageTabInfo) {
        _selectedPage.value = tabInfo
    }

}