package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.response.home.HomeBanner
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.utils.NetworkResponse
import com.cmc12th.runway.utils.PagingNetworkResponse
import retrofit2.http.*

interface HomeService {

    /** 홈화면 쇼룸 조회 */
    /** @param type 홈화면 조회시 0 전체보기 조회 시 1
     *  홈화면은 총 10개, 전체는 총 30개가 보여짐 */
    @GET("/home")
    suspend fun getHomeBanner(@Query("type") type: Int): NetworkResponse<HomeBanner>

    /** 홈화면 리뷰 조회 */
    @GET("/home/review")
    suspend fun getHomeReview(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<HomeReviewItem>

    /** 홈화면 리뷰 상세 조회 */
    @GET("/home/review/detail/{reviewId}")
    suspend fun getHomeReviewDetail(
        @Path("reviewId") reviewId: Int,
    ): NetworkResponse<UserReviewDetail>

}

