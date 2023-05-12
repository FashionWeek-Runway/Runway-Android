package com.cmc12th.runway.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.runway.data.pagingsource.MapInfoItemByFilterPagingSource
import com.cmc12th.runway.data.pagingsource.MapInfoItemByLocationSearchPagingSource
import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.PagingApiWrapper
import com.cmc12th.domain.model.response.map.*
import com.cmc12th.domain.model.safeFlow
import com.cmc12th.domain.model.safePagingFlow
import com.cmc12th.domain.repository.MapRepository
import com.cmc12th.runway.data.network.RunwayClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : MapRepository {
    override fun mapFiltering(mapFilterRequest: com.cmc12th.domain.model.request.map.MapFilterRequest): Flow<ApiWrapper<List<MapFilterItem>>> =
        safeFlow {
            runwayClient.mapsFiltering(mapFilterRequest)
        }

    override fun mapInfoPaging(
        page: Int,
        size: Int,
        mapFilterRequest: com.cmc12th.domain.model.request.map.MapFilterRequest,
    ): Flow<PagingApiWrapper<MapInfoItem>> = safePagingFlow {
        runwayClient.mapsInfoPaging(page, size, mapFilterRequest)
    }

    override fun getMpaInfoPagingItem(mapFilterRequest: com.cmc12th.domain.model.request.map.MapFilterRequest): Flow<PagingData<MapInfoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                MapInfoItemByFilterPagingSource(
                    mapRepository = this,
                    mapFilterRequest = mapFilterRequest
                )
            },
        ).flow
    }

    override fun mapSearch(mapSearchRequest: com.cmc12th.domain.model.request.map.MapSearchRequest): Flow<ApiWrapper<MapSearchResponseBody>> =
        safeFlow {
            runwayClient.mapSearch(mapSearchRequest)
        }

    override fun mapInfo(storeId: Int): Flow<ApiWrapper<MapInfoItem>> = safeFlow {
        runwayClient.mapInfo(storeId)
    }

    override fun storeSearch(storeId: Int): Flow<ApiWrapper<StoreInfoWithMarkerData>> = safeFlow {
        runwayClient.storeSearch(storeId)
    }

    override fun locationMarkerSearch(regionId: Int): Flow<ApiWrapper<Array<MapMarker>>> =
        safeFlow {
            runwayClient.mapRegionMarkerInfo(regionId)
        }

    override fun locationInfoPaging(
        regionId: Int,
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<StoreInfo>> = safePagingFlow {
        runwayClient.mapRegionInfoPaging(regionId, page, size)
    }

    override fun getLocationInfoPagingItem(regionId: Int): Flow<PagingData<MapInfoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                MapInfoItemByLocationSearchPagingSource(
                    mapRepository = this,
                    regionId = regionId,
                )
            },
        ).flow
    }
}