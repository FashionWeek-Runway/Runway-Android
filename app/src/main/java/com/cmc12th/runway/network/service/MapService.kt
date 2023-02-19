package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.MapFilterResponse
import com.cmc12th.runway.data.response.map.MapInfoPagingResponse
import com.cmc12th.runway.data.response.map.MapSearchResponse
import com.cmc12th.runway.utils.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MapService {

    /** 메인 지도 조회 + 필터링 조회 */
    @POST("/maps/filter")
    suspend fun mapsFiltering(@Body mapFilterRequest: MapFilterRequest): NetworkResponse<MapFilterResponse>

    /** 스와이프 쇼룸 필터링 조회 */
    @POST("/maps/info")
    suspend fun mapsInfoPaging(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body mapFilterRequest: MapFilterRequest,
    ): NetworkResponse<MapInfoPagingResponse>

    /** 지도 쇼룸 검색 지도 조회 검색용 */
    @POST("/maps/search")
    suspend fun mapsSearch(@Body mapSearchRequest: MapSearchRequest): NetworkResponse<MapSearchResponse>

}