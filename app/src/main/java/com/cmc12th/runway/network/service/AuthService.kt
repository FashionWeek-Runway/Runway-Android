package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.request.auth.PasswordRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.data.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.data.response.user.*
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.NetworkResponse
import com.cmc12th.runway.utils.PagingNetworkResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    /** 로그아웃 */
    @GET("/users/logout")
    suspend fun logout(): DefaultResponse

    /** 리프레쉬 토큰 유효성 검증  */
    @POST("/login/refresh")
    suspend fun loginRefresh(@Header("X-REFRESH-TOKEN") refreshToken: String): NetworkResponse<LoginResponse>

    /** 토큰 재발급  */
    @POST("/users/refresh")
    suspend fun tokenReissuance(@Header("X-REFRESH-TOKEN") refreshToken: String): Call<ResponseWrapper<OauthLoginRequest>>

    /** 마이페이지 조회 */
    @GET("/users")
    suspend fun getMyInfo(): NetworkResponse<MyPageInfo>

    /** 내가 북마크한 리스트 조회 */
    suspend fun getBookmarkedReview(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<MyReviewsItem>

    /** 내가 북마크한 리뷰 상세 조회 */
    @GET("/users/bookmark/review/detail/{reviewId}")
    suspend fun getMyBookmarkedReviewDetail(
        @Path("reviewId") reviewId: Int,
    ): NetworkResponse<UserReviewDetail>

    /** 개인정보 관리 조회 */
    @GET("/users/info")
    suspend fun getInformationManagementInfo(): NetworkResponse<UserInformationManagamentInfo>

    /** 개인정보 카카오 연동 */
    @POST("/users/info/kakao")
    suspend fun linkToKakao(@Body oauthLoginRequest: OauthLoginRequest): DefaultResponse

    /** 개인정보 카카오 연동헤제 */
    @DELETE("/users/info/kakao")
    suspend fun unLinkToKakao(): DefaultResponse

    /** 프로필 편집을 위한 기존 데이터 조회 */
    @GET("/users/profile")
    suspend fun getProfileInfoToEdit(): NetworkResponse<ImgUrlAndNickname>

    /** 프로필 편집을 위한 기존 데이터 조회 */
    @Multipart
    @PATCH("/users/profile")
    suspend fun patchProfileImage(
        @Part basic: MultipartBody.Part?,
        @Part multipartFile: MultipartBody.Part?,
        @Part nickname: MultipartBody.Part?,
    ): NetworkResponse<ImgUrlAndNicknameAndCategorys>

    /** 내가 작성한 리뷰 조회 */
    @GET("/users/review")
    suspend fun getMyReview(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<MyReviewsItem>

    /** 내가 작성한 리뷰 상세조회 */
    @GET("/users/review/detail/{reviewId}")
    suspend fun getMyReviewDetail(
        @Path("reviewId") reviewId: Int,
    ): NetworkResponse<UserReviewDetail>

    /** 내가 북마크한 쇼룸 리스트 보기 */
    @GET("/users/store")
    suspend fun getBookmarkedStore(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PagingNetworkResponse<StoreMetaDataItem>

    /** 내 카테고리 조회 */
    @GET("/home/categories")
    suspend fun getCategories(): NetworkResponse<ArrayList<String>>

    /** 홈화면 카테고리 선택 */
    @PATCH("/home/categories")
    suspend fun setCategories(@Body patchCategoryBody: PatchCategoryBody): DefaultResponse

    @POST("/users/password")
    suspend fun verifyPassword(@Body passwordRequest: PasswordRequest): DefaultResponse

    @PATCH("/users/password")
    suspend fun modifyPassword(@Body passwordRequest: PasswordRequest): DefaultResponse


}