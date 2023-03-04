package com.cmc12th.runway.domain.repository

import androidx.paging.PagingData
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.home.HomeBanner
import com.cmc12th.runway.data.response.home.HomeReviewItem
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

interface HomeRepository {
    fun getHomeBanner(type: Int): Flow<ApiWrapper<HomeBanner>>

    fun getHomeReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<HomeReviewItem>>

    fun getHomeReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>>
}