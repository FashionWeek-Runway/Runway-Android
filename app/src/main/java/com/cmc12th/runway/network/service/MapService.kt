package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.response.map.MapFilterItem
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.data.response.map.MapSearchResponseBody
import com.cmc12th.runway.utils.NetworkResponse
import com.cmc12th.runway.utils.PagingNetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

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

}