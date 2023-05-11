package com.cmc12th.runway.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.network.model.ApiState
import com.cmc12th.runway.utils.Constants
import kotlinx.coroutines.flow.first


class MyReviewPagingSource(
    private val authRepository: AuthRepository
) : PagingSource<Int, MyReviewsItem>() {

    override fun getRefreshKey(state: PagingState<Int, MyReviewsItem>): Int? {
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyReviewsItem> {
        val position = params.key ?: Constants.INIT_PAGE_INDEX
        val loadData =
            authRepository.getMyReview(
                page = position,
                size = params.loadSize,
            ).first()

        try {
            if (loadData is ApiState.Success) {
                return LoadResult.Page(
                    data = loadData.data.pagingMetadata.contents,
                    prevKey = if (position == Constants.INIT_PAGE_INDEX) null else position - 1,
                    nextKey = if (loadData.data.pagingMetadata.isLast) null else position + 1,
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return LoadResult.Error(IllegalArgumentException("페이징 데이터를 불러올 수 없습니다."))
    }
}