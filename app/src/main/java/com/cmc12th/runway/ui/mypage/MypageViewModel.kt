package com.cmc12th.runway.ui.mypage

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.ACCESS_TOKEN
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.REFRESH_TOKEN
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _myReviews = MutableStateFlow<PagingData<MyReviewsItem>>(PagingData.empty())
    val myReviews: StateFlow<PagingData<MyReviewsItem>> = _myReviews.asStateFlow()
    private val _bookmarkedStore =
        MutableStateFlow<PagingData<StoreMetaDataItem>>(PagingData.empty())
    val bookmarkedStore: StateFlow<PagingData<StoreMetaDataItem>> = _bookmarkedStore.asStateFlow()
    val onDetail = mutableStateOf(DetailState.default())
    val selectedPage = mutableStateOf(MypageTabInfo.MY_REVIEW)

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

}