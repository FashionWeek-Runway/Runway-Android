package com.cmc12th.runway.ui.detail

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.utils.toPlainRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
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

    fun addUserReview(storeId: Int, bitmap: Bitmap, onSuccess: () -> Unit) = viewModelScope.launch {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        val encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        storeRepository.writeUserReview(
            storeId,
            MultipartBody.Part.createFormData(
                "img",
                Math.random().toString(),
                encodedImage.toRequestBody()
            )
        ).collect { apiState ->
            apiState.onSuccess {
                onSuccess()
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

    fun updateBookmarkState(isBookmarked: Boolean) {
        _storeDetail.value = _storeDetail.value.copy(
            bookmark = isBookmarked
        )
    }

    fun getUserReviewPaging(storeId: Int) = viewModelScope.launch {
        storeRepository.userReviewPaging(storeId).cachedIn(viewModelScope).collect {
            _userReviews.value = it
        }
    }

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