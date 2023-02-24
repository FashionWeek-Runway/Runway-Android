package com.cmc12th.runway.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.network.model.ApiState
import com.cmc12th.runway.utils.Constants.INIT_PAGE_INDEX
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.flow.first

class UserReviewPagingSource(
    private val storeId: Int,
    private val storeRepository: StoreRepository
) : PagingSource<Int, UserReview>() {

    override fun getRefreshKey(state: PagingState<Int, UserReview>): Int? {
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserReview> {
        val position = params.key ?: INIT_PAGE_INDEX
        val loadData =
            storeRepository.getUserReview(
                page = position,
                size = params.loadSize,
                storeId = storeId,
            ).first()

        try {
            if (loadData is ApiState.Success) {
                return LoadResult.Page(
                    data = loadData.data.pagingMetadata.contents,
                    prevKey = if (position == INIT_PAGE_INDEX) null else position - 1,
                    nextKey = if (loadData.data.pagingMetadata.isLast) null else position + 1,
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return LoadResult.Error(IllegalArgumentException("페이징 데이터를 불러올 수 없습니다."))
    }
}