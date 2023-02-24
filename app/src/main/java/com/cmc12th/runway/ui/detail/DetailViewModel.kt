package com.cmc12th.runway.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.pagingsource.UserReviewPagingSource
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.ui.detail.view.BlogReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val storeDetail: StoreDetail = StoreDetail(),
    val blogReview: List<BlogReview> = emptyList()
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _storeDetail = MutableStateFlow(StoreDetail())
    private val _blogReview = MutableStateFlow(emptyList<BlogReview>())
    private val _userReviews =
        MutableStateFlow<PagingData<UserReview>>(PagingData.empty())
    val userReviews: StateFlow<PagingData<UserReview>> =
        _userReviews.asStateFlow()

    val uiState = combine(
        _storeDetail, _blogReview
    ) { flowArr ->
        DetailUiState(
            storeDetail = flowArr[0] as StoreDetail,
            blogReview = flowArr[1] as List<BlogReview>
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailUiState()
    )


    fun getUserReviewPaging(storeId: Int) = viewModelScope.launch {
        storeRepository.userReviewPaging(storeId).cachedIn(viewModelScope).collect {
            _userReviews.value = it
        }
    }

//    fun getUserReviewPaging(storeId: Int) = Pager(
//        config = PagingConfig(
//            pageSize = 10,
//        ),
//        pagingSourceFactory = {
//            UserReviewPagingSource(
//                storeId = storeId,
//                storeRepository = storeRepository
//            )
//        },
//    ).flow

    fun getDetailInfo(idx: Int) = viewModelScope.launch {
        storeRepository.getDetail(idx).collect { apiState ->
            apiState.onSuccess {
                _storeDetail.value = it.result
            }
        }
    }

    fun getBlogReview(idx: Int, storeName: String) = viewModelScope.launch {
        storeRepository.getBlogReview(idx, storeName).collect { apiState ->
            apiState.onSuccess {
                _blogReview.value = it.result
            }
        }
    }

}