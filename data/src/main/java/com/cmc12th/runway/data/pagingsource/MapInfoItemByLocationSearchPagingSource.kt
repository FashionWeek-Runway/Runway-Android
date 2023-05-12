package com.cmc12th.runway.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmc12th.domain.model.ApiState
import com.cmc12th.domain.model.response.map.MapInfoItem
import com.cmc12th.domain.repository.MapRepository
import com.cmc12th.runway.data.Constants
import kotlinx.coroutines.flow.first

class MapInfoItemByLocationSearchPagingSource(
    private val regionId: Int,
    private val mapRepository: MapRepository,
) : PagingSource<Int, MapInfoItem>() {

    override fun getRefreshKey(state: PagingState<Int, MapInfoItem>): Int? {
        return state.anchorPosition?.let { achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MapInfoItem> {
        val position = params.key ?: Constants.INIT_PAGE_INDEX
        val loadData =
            mapRepository.locationInfoPaging(
                page = position,
                size = params.loadSize,
                regionId = regionId,
            ).first()
        try {
            if (loadData is ApiState.Success) {
                return LoadResult.Page(
                    data = loadData.data.pagingMetadata.contents.map {
                        it.toMapInfoItem()
                    },
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