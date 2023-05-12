package com.cmc12th.runway.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.runway.data.pagingsource.HomeReviewItemPagingSource
import com.cmc12th.domain.model.response.home.HomeBanner
import com.cmc12th.domain.model.response.home.HomeReviewItem
import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.DefaultApiWrapper
import com.cmc12th.domain.PagingApiWrapper
import com.cmc12th.domain.model.response.store.UserReviewDetail
import com.cmc12th.domain.model.response.user.PatchCategoryBody
import com.cmc12th.domain.model.safeFlow
import com.cmc12th.domain.model.safePagingFlow
import com.cmc12th.domain.repository.HomeRepository
import com.cmc12th.runway.data.network.RunwayClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : HomeRepository {
    override fun getHomeBanner(type: Int): Flow<ApiWrapper<HomeBanner>> =
        safeFlow {
            runwayClient.getHomeBanner(type)
        }

    override fun getHomeReview(
        page: Int,
        size: Int
    ): Flow<PagingApiWrapper<HomeReviewItem>> =
        safePagingFlow {
            runwayClient.getHomeReview(page, size)
        }

    override fun getHomeReviewPaging(): Flow<PagingData<com.cmc12th.domain.model.response.home.HomeReviewItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                HomeReviewItemPagingSource(
                    homeRepository = this
                )
            },
        ).flow
    }

    override fun getHomeReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>> = safeFlow {
        runwayClient.getHomeReviewDetail(reviewId)
    }

    override fun getCategories(): Flow<ApiWrapper<ArrayList<String>>> = safeFlow {
        runwayClient.getCategorys()
    }

    override fun setCategories(patchCategoryBody: PatchCategoryBody): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.setCategorys(patchCategoryBody)
        }

}