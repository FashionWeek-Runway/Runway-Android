package com.cmc12th.runway.ui.mypage

import android.util.Log
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

    fun logout(onSuccess: () -> Unit) = viewModelScope.launch {
        authRepository.setToken(ACCESS_TOKEN, "")
        authRepository.setToken(REFRESH_TOKEN, "")
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("dlgocks1", "연결 끊기 실패", error)
            } else {
                Log.i("dlgocks1", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
        authRepository.logout().collect {
            it.onSuccess {
                onSuccess()
            }
        }
    }

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