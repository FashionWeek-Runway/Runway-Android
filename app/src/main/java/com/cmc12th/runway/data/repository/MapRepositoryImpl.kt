package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.MapFilterItem
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.data.response.map.MapSearchResponseBody
import com.cmc12th.runway.data.response.map.StoreInfoWithMarkerData
import com.cmc12th.runway.domain.repository.MapRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.safeFlow
import com.cmc12th.runway.network.model.safePagingFlow
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : MapRepository {
    override fun mapFiltering(mapFilterRequest: MapFilterRequest): Flow<ApiWrapper<List<MapFilterItem>>> =
        safeFlow {
            runwayClient.mapsFiltering(mapFilterRequest)
        }

    override fun mapInfoPaging(
        page: Int,
        size: Int,
        mapFilterRequest: MapFilterRequest,
    ): Flow<PagingApiWrapper<MapInfoItem>> = safePagingFlow {
        runwayClient.mapsInfoPaging(page, size, mapFilterRequest)
    }

    override fun mapSearch(mapSearchRequest: MapSearchRequest): Flow<ApiWrapper<MapSearchResponseBody>> =
        safeFlow {
            runwayClient.mapSearch(mapSearchRequest)
        }

    override fun mapInfo(storeId: Int): Flow<ApiWrapper<MapInfoItem>> = safeFlow {
        runwayClient.mapInfo(storeId)
    }

    override fun storeSearch(storeId: Int): Flow<ApiWrapper<StoreInfoWithMarkerData>> = safeFlow {
        runwayClient.storeSearch(storeId)
    }
}