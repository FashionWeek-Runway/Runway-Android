package com.cmc12th.runway.ui.detail.photoreview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    //    private val _reviewDetail = MutableStateFlow(UserReviewDetail.default())
    private val _reviewDetail = mutableStateOf(UserReviewDetail.default())
    val reviewDetail: State<UserReviewDetail> get() = _reviewDetail

    fun getReviewDetail(reviewId: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        storeRepository.getReviewDetail(reviewId).collect { apiState ->
            apiState.onSuccess {
                _reviewDetail.value = it.result
                onSuccess()
            }
        }
    }

    fun updateBookmark(reviewId: Int, onSuccess: () -> Unit) = viewModelScope.launch {
        storeRepository.reviewBookmark(reviewId).collect {
            it.onSuccess {
                _reviewDetail.value = _reviewDetail.value.copy(
                    bookmark = !_reviewDetail.value.bookmark,
                    bookmarkCnt = if (_reviewDetail.value.bookmark) _reviewDetail.value.bookmarkCnt - 1 else _reviewDetail.value.bookmarkCnt + 1
                )
                onSuccess()
            }
        }
    }

    fun reporteReview(reviewId: Int) = viewModelScope.launch {
        storeRepository.reportReview(reviewReportRequest = ReviewReportRequest(
            opinion = "",
            reviewId = reviewId,
            reason = 0,
        )).collect {
            it.onSuccess {

            }
        }
    }

}