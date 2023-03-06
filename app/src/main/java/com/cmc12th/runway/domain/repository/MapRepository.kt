package com.cmc12th.runway.domain.repository

import androidx.paging.PagingData
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.*
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface MapRepository {
    fun mapFiltering(mapFilterRequest: MapFilterRequest): Flow<ApiWrapper<List<MapFilterItem>>>

    fun mapInfoPaging(
        page: Int, size: Int, mapFilterRequest: MapFilterRequest,
    ): Flow<PagingApiWrapper<MapInfoItem>>

    fun mapSearch(mapSearchRequest: MapSearchRequest):
            Flow<ApiWrapper<MapSearchResponseBody>>

    fun mapInfo(@Path("storeId") storeId: Int): Flow<ApiWrapper<MapInfoItem>>

    fun storeSearch(@Path("storeId") storeId: Int): Flow<ApiWrapper<StoreInfoWithMarkerData>>

    fun locationMarkerSearch(regionId: Int): Flow<ApiWrapper<Array<MapMarker>>>

    fun locationInfoPaging(
        regionId: Int,
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<StoreInfo>>

    fun getMpaInfoPagingItem(mapFilterRequest: MapFilterRequest): Flow<PagingData<MapInfoItem>>
    fun getLocationInfoPagingItem(regionId: Int): Flow<PagingData<MapInfoItem>>
}