package com.cmc12th.runway.ui.detail.photoreview

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.model.ReviewReportType
import com.cmc12th.domain.model.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.repository.HomeRepository
import com.cmc12th.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewReportUiState(
    val reports: List<ReviewReportWrapper> = com.cmc12th.model.ReviewReportType.values().map {
        ReviewReportWrapper(it.id, it.reason)
    },
    val reportContents: String = "",
    val selectedReportId: Int = -1,
)

data class ReviewReportWrapper(
    val id: Int,
    val contents: String,
    var isSelected: Boolean = false,
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storeRepository: com.cmc12th.domain.repository.StoreRepository,
    private val authRepository: com.cmc12th.domain.repository.AuthRepository,
    private val homeRepository: com.cmc12th.domain.repository.HomeRepository,
) : ViewModel() {

    private val _reviewDetail = mutableStateOf(UserReviewDetail.default())
    val reviewDetail: State<UserReviewDetail> get() = _reviewDetail

    private val _report: MutableStateFlow<List<ReviewReportWrapper>> = MutableStateFlow(
        com.cmc12th.model.ReviewReportType.values().map {
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

    /** 디테일 탭 -> 매장에서의 리뷰 디테일 조회 */
    fun getReviewDetailStore(reviewId: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        storeRepository.getReviewDetail(reviewId).collect { apiState ->
            apiState.onSuccess {
                _reviewDetail.value = it.result
                onSuccess()
            }
        }
    }

    /** 홈에서의 리뷰 디테일 조회 */
    fun getReviewDetailHome(reviewId: Int, onSuccess: () -> Unit = {}) =
        viewModelScope.launch {
            homeRepository.getHomeReviewDetail(reviewId).collect { apiState ->
                apiState.onSuccess {
                    _reviewDetail.value = it.result
                    onSuccess()
                }
            }
        }

    /** 마이페이지에서의 내가 올린 리뷰 디테일 조회 */
    fun getReviewDetailMypage(reviewId: Int, onSuccess: () -> Unit = {}) =
        viewModelScope.launch {
            authRepository.getMyReviewDetail(reviewId).collect { apiState ->
                apiState.onSuccess {
                    _reviewDetail.value = it.result
                    onSuccess()
                }
            }
        }

    /** 마이페이지 내가 북마크한 리뷰 디테일 상세 조회 */
    fun getReviewDetailBookmark(reviewId: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        authRepository.getMyBookmarkedReviewDetail(reviewId).collect { apiState ->
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
            reviewReportRequest = com.cmc12th.domain.model.request.store.ReviewReportRequest(
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