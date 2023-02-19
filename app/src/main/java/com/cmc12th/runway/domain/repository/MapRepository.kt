package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.MapFilterItem
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.data.response.map.MapSearchResponseBody
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun mapFiltering(mapFilterRequest: MapFilterRequest): Flow<ApiWrapper<List<MapFilterItem>>>

    fun mapInfoPaging(
        page: Int, size: Int, mapFilterRequest: MapFilterRequest,
    ): Flow<PagingApiWrapper<MapInfoItem>>

    fun mapSearch(mapSearchRequest: MapSearchRequest):
            Flow<ApiWrapper<MapSearchResponseBody>>
}