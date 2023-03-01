package com.cmc12th.runway.domain.repository

import androidx.paging.PagingData
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.*

interface StoreRepository {
    fun storeBookmark(storeId: Int): Flow<DefaultApiWrapper>

    fun getBlogReview(storeId: Int, storeName: String): Flow<ApiWrapper<List<BlogReview>>>

    fun getDetail(storeId: Int): Flow<ApiWrapper<StoreDetail>>

    fun getUserReview(
        storeId: Int,
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<UserReview>>

    fun writeUserReview(
        storeId: Int, img: RequestBody,
    ): Flow<DefaultApiWrapper>

    fun userReviewPaging(storeId: Int): Flow<PagingData<UserReview>>

    fun getReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>>

    fun reviewBookmark(reviewId: Int): Flow<DefaultApiWrapper>

    fun reportReview(reviewReportRequest: ReviewReportRequest): Flow<DefaultApiWrapper>

    fun delteReview(reviewId: Int): Flow<DefaultApiWrapper>
}