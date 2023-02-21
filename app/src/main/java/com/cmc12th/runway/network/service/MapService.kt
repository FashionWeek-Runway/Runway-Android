package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.*
import com.cmc12th.runway.utils.NetworkResponse
import com.cmc12th.runway.utils.PagingNetworkResponse
import retrofit2.http.*

interface MapService {

    /** 메인 지도 조회 + 필터링 조회 */
    @POST("/maps/filter")
    suspend fun mapFiltering(@Body mapFilterRequest: MapFilterRequest): NetworkResponse<List<MapFilterItem>>

    /** 스와이프 쇼룸 필터링 조회 */
    @POST("/maps/info")
    suspend fun mapInfoPaging(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body mapFilterRequest: MapFilterRequest,
    ): PagingNetworkResponse<MapInfoItem>

    /** 지도 쇼룸 검색 지도 조회 검색용 */
    @POST("/maps/search")
    suspend fun mapSearch(@Body mapSearchRequest: MapSearchRequest): NetworkResponse<MapSearchResponseBody>

    /** 지도 매장 단일 선택 하단 스와이프조회  */
    @GET("/maps/info/{storeId}")
    suspend fun mapInfo(@Path("storeId") storeId: Int): NetworkResponse<MapInfoItem>

    /** 지도 쇼룸 검색 (쇼룸 마커 + 하단 스와이프 조회) */
    @GET("/maps/{storeId}")
    suspend fun mapSearchInfo(@Path("storeId") storeId: Int): NetworkResponse<StoreInfoWithMarkerData>

}