package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.NetworkResponse
import com.cmc12th.runway.utils.PagingNetworkResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface StoreService {

    /** 쇼룸 북마크 */
    @POST("/stores/{storeId}")
    suspend fun storeBookmark(@Path("storeId") storeId: Int): DefaultResponse

    /** 쇼룸 웹 스크랩핑(블로그 리뷰) */
    @GET("/stores/blog/{storeId}")
    suspend fun getBlogReview(
        @Path("storeId") storeId: Int,
        @Query("storeName") storeName: String,
    ): NetworkResponse<List<BlogReview>>

    /** 쇼룸 사장님 소식 리스트 조회 */
//    @POST("/stores/board/{storeId}")
//    suspend fun getStoreManagerNews(
//        @Path("storeId") storeId: Int,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//    )

    /** 쇼룸 상세 페이지 상단 정보 */
    @GET("/stores/detail/{storeId}")
    suspend fun getDetail(@Path("storeId") storeId: Int): NetworkResponse<StoreDetail>

    /** 쇼룸 사용자 후기 */
    @GET("/stores/review/{storeId}")
    suspend fun getUserReview(
        @Path("storeId") storeId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<UserReview>

    /** 쇼룸 후기 작성 */
    @POST("/stores/review/{storeId}")
    suspend fun writeUserReview(
        @Path("storeId") storeId: Int,
        @Body img: RequestBody,
    ): DefaultResponse

    /** 쇼룸 후기 상세 조회 */
    @GET("/stores/review/detail/{reviewId}")
    suspend fun getReviewDetail(
        @Path("reviewId") reviewId: Int,
    ): NetworkResponse<UserReviewDetail>

    /** 리뷰 묵마크 */
    @POST("/stores/review/bookmark/{reviewId}")
    suspend fun reviewBookmark(
        @Path("reviewId") reviewId: Int,
    ): DefaultResponse

    /** 리뷰 삭제 */
    @PATCH("/stores/review/detail/{reviewId}")
    suspend fun delteReview(
        @Path("reviewId") reviewId: Int,
    ): DefaultResponse

    /** 리뷰 신고 */
    @POST("/stores/review/report")
    suspend fun reportReview(
        @Body reviewReportRequest: ReviewReportRequest,
    ): DefaultResponse
}

