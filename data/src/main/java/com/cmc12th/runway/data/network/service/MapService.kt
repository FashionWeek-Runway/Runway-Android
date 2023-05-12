package com.cmc12th.runway.data.network.service

import com.cmc12th.domain.NetworkResponse
import com.cmc12th.domain.PagingNetworkResponse
import com.cmc12th.domain.model.response.map.*
import retrofit2.http.*

interface MapService {

    /** 메인 지도 조회 + 필터링 조회 */
    @POST("/maps/filter")
    suspend fun mapFiltering(@Body mapFilterRequest: com.cmc12th.domain.model.request.map.MapFilterRequest): NetworkResponse<List<MapFilterItem>>

    /** 스와이프 쇼룸 필터링 조회 */
    @POST("/maps/info")
    suspend fun mapInfoPaging(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body mapFilterRequest: com.cmc12th.domain.model.request.map.MapFilterRequest,
    ): PagingNetworkResponse<MapInfoItem>

    /** 지도 쇼룸 검색 지도 조회 검색용 */
    @POST("/maps/search")
    suspend fun mapSearch(@Body mapSearchRequest: com.cmc12th.domain.model.request.map.MapSearchRequest): NetworkResponse<MapSearchResponseBody>

    /** 지도 매장 단일 선택 하단 스와이프조회  */
    @GET("/maps/info/{storeId}")
    suspend fun mapInfo(@Path("storeId") storeId: Int): NetworkResponse<MapInfoItem>

    /** 지도 쇼룸 검색 (쇼룸 마커 + 하단 스와이프 조회) */
    @GET("/maps/{storeId}")
    suspend fun mapSearchInfo(@Path("storeId") storeId: Int): NetworkResponse<StoreInfoWithMarkerData>

    /** 쇼룸 지역 검색 마커 결과 */
    @GET("/maps/region/{regionId}")
    suspend fun mapRegionMarkerInfo(@Path("regionId") regionId: Int): NetworkResponse<Array<MapMarker>>

    /** 쇼룸 지역 검색 하단 스와이프 */
    @GET("/maps/info/region/{regionId}")
    suspend fun mapRegionInfoPaging(
        @Path("regionId") regionId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<StoreInfo>
}