package com.cmc12th.runway.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.runway.data.pagingsource.UserReviewPagingSource
import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.DefaultApiWrapper
import com.cmc12th.domain.PagingApiWrapper
import com.cmc12th.domain.model.response.store.BlogReview
import com.cmc12th.domain.model.response.store.StoreDetail
import com.cmc12th.domain.model.response.store.UserReview
import com.cmc12th.domain.model.response.store.UserReviewDetail
import com.cmc12th.domain.model.safeFlow
import com.cmc12th.domain.model.safePagingFlow
import com.cmc12th.domain.repository.StoreRepository
import com.cmc12th.runway.data.network.RunwayClient
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : StoreRepository {

    override fun storeBookmark(storeId: Int): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.storeBookmark(storeId)
    }

    override fun getBlogReview(
        storeId: Int,
        storeName: String,
    ): Flow<ApiWrapper<List<BlogReview>>> = safeFlow {
        runwayClient.getBlogReview(storeId, storeName)
    }

    override fun getDetail(storeId: Int): Flow<ApiWrapper<StoreDetail>> = safeFlow {
        runwayClient.getDetail(storeId)
    }

    override fun getUserReview(
        storeId: Int,
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<UserReview>> = safePagingFlow {
        runwayClient.getUserReview(storeId, page, size)
    }

    override fun writeUserReview(
        storeId: Int,
        img: RequestBody,
    ): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.writeUserReview(storeId, img)
        }

    override fun userReviewPaging(storeId: Int): Flow<PagingData<UserReview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                UserReviewPagingSource(
                    storeId = storeId,
                    storeRepository = this
                )
            },
        ).flow
    }

    override fun getReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>> = safeFlow {
        runwayClient.getReviewDetail(reviewId)
    }

    override fun reviewBookmark(reviewId: Int): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.reviewBookmark(reviewId)
    }

    override fun delteReview(reviewId: Int): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.delteReview(reviewId)
    }

    override fun reportReview(reviewReportRequest: com.cmc12th.domain.model.request.store.ReviewReportRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.reportReview(reviewReportRequest)
        }

}