package com.cmc12th.runway.ui.detail.photoreview

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.model.ReviewReportType
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewReportUiState(
    val reports: List<ReviewReportWrapper> = ReviewReportType.values().map {
        ReviewReportWrapper(it.id, it.reason)
    },
    val reportContents: String = "",
    val selectedReportId: Int = -1
)

data class ReviewReportWrapper(
    val id: Int,
    val contents: String,
    var isSelected: Boolean = false,
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    //    private val _reviewDetail = MutableStateFlow(UserReviewDetail.default())
    private val _reviewDetail = mutableStateOf(UserReviewDetail.default())
    val reviewDetail: State<UserReviewDetail> get() = _reviewDetail

    private val _report: MutableStateFlow<List<ReviewReportWrapper>> = MutableStateFlow(
        ReviewReportType.values().map {
            ReviewReportWrapper(it.id, it.reason)
        }
    )
    private val _reportContents: MutableStateFlow<String> = MutableStateFlow<String>("")
    private var _selectedReportId = MutableStateFlow(-1)

    val reportUiState = combine(
        _report, _reportContents, _selectedReportId
    ) { report, reportContents, selectedReportIdx ->
        ReviewReportUiState(
            reports = report,
            reportContents = reportContents,
            selectedReportId = selectedReportIdx
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReviewReportUiState()
    )


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

    fun deleteReview(reviewId: Int, onSuccess: () -> Unit) = viewModelScope.launch {
        storeRepository.delteReview(reviewId = reviewId).collect {
            it.onSuccess {
                onSuccess()
            }
        }
    }

    fun reporteReview(reviewId: Int, onSuccess: () -> Unit) = viewModelScope.launch {
        storeRepository.reportReview(
            reviewReportRequest = ReviewReportRequest(
                opinion = _reportContents.value,
                reviewId = reviewId,
                reason = _selectedReportId.value,
            )
        ).collect {
            it.onSuccess {
                onSuccess()
            }
        }
    }

    fun updateSelectedId(id: Int) {
        _selectedReportId.value = id
    }

    fun updateReportContents(str: String) {
        _reportContents.value = str
    }

}