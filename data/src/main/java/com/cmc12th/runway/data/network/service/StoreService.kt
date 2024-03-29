package com.cmc12th.runway.data.network.service

import com.cmc12th.domain.DefaultResponse
import com.cmc12th.domain.NetworkResponse
import com.cmc12th.domain.PagingNetworkResponse
import com.cmc12th.domain.model.request.store.StoreReportRequest
import com.cmc12th.domain.model.response.store.BlogReview
import com.cmc12th.domain.model.response.store.StoreDetail
import com.cmc12th.domain.model.response.store.UserReview
import com.cmc12th.domain.model.response.store.UserReviewDetail
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
        @Body reviewReportRequest: com.cmc12th.domain.model.request.store.ReviewReportRequest,
    ): DefaultResponse

    /** 상점 잘못된 정보신고 */
    @POST("/stores/report/{storeId}")
    suspend fun reportStore(
        @Path("storeId") storeId: Int,
        @Body storeReportRequest: StoreReportRequest,
    ): DefaultResponse
}

