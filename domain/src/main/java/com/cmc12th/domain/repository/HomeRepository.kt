package com.cmc12th.domain.repository

import androidx.paging.PagingData
import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.DefaultApiWrapper
import com.cmc12th.domain.PagingApiWrapper
import com.cmc12th.domain.model.response.home.HomeBanner
import com.cmc12th.domain.model.response.home.HomeReviewItem
import com.cmc12th.domain.model.response.store.UserReviewDetail
import com.cmc12th.domain.model.response.user.PatchCategoryBody
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