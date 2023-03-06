package com.cmc12th.runway.domain.repository

import androidx.paging.PagingData
import com.cmc12th.runway.data.response.home.HomeBanner
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.data.response.user.PatchCategoryBody
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeBanner(type: Int): Flow<ApiWrapper<HomeBanner>>

    fun getHomeReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<HomeReviewItem>>

    fun getHomeReviewPaging(): Flow<PagingData<HomeReviewItem>>

    fun getHomeReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>>
    fun setCategories(patchCategoryBody: PatchCategoryBody): Flow<DefaultApiWrapper>
    fun getCategories(): Flow<ApiWrapper<ArrayList<String>>>
}