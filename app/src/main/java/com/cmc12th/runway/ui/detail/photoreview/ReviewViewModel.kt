package com.cmc12th.runway.ui.detail.photoreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    fun getReviewDetail(reviewId: Int) = viewModelScope.launch {
        storeRepository.getReviewDetail(reviewId).collect {
            // TODO 응답값이 바뀔 예정이라 나중에
        }
    }
}