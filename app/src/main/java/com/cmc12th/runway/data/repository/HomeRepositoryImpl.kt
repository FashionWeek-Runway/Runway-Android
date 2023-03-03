package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.response.home.HomeBanner
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.domain.repository.HomeRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.safeFlow
import com.cmc12th.runway.network.model.safePagingFlow
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : HomeRepository {
    override fun getHomeBanner(type: Int): Flow<ApiWrapper<HomeBanner>> = safeFlow {
        runwayClient.getHomeBanner(type)
    }

    override fun getHomeReview(page: Int, size: Int): Flow<PagingApiWrapper<HomeReviewItem>> =
        safePagingFlow {
            runwayClient.getHomeReview(page, size)
        }

}