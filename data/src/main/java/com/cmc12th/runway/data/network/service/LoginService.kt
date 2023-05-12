package com.cmc12th.runway.data.network.service

import com.cmc12th.domain.model.response.LoginResponse
import com.cmc12th.domain.model.response.SignUpResponse
import com.cmc12th.domain.DefaultResponse
import com.cmc12th.domain.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface LoginService {

    /** 로그인 */
    @POST("/login")
    suspend fun login(@Body loginRequest: com.cmc12th.domain.model.request.LoginRequest): NetworkResponse<LoginResponse>

    /** 유저 인증번호 확인 */
    @POST("/login/check")
    suspend fun verifyPhoneNumber(@Body loginCheckRequest: com.cmc12th.domain.model.request.LoginCheckRequest): DefaultResponse

    /** 닉네임 중복체크 */
    @GET("/login/check/nickname")
    suspend fun checkNickname(@Query("nickname") nickname: String): DefaultResponse

    /** 카카오 로그인 */
    @POST("/login/kakao")
    suspend fun kakaoLogin(@Body oauthLoginRequest: com.cmc12th.domain.model.request.OauthLoginRequest): NetworkResponse<LoginResponse>

    /** 유저 비밀번호 변경 */
    @POST("/login/phone")
    suspend fun modifyPassword(@Body passwordAndPhoneNumberRequest: com.cmc12th.domain.model.request.PasswordAndPhoneNumberRequest): DefaultResponse

    /** 유저 인증번호 전송 */
    @POST("/login/send")
    suspend fun sendVerifyMessage(@Body sendVerifyMessageRequest: com.cmc12th.domain.model.request.SendVerifyMessageRequest): DefaultResponse

    /** 회원가입 */
    @Multipart
    @POST("/login/signup")
    suspend fun signUp(
        @PartMap feedPostReqeust: HashMap<String, RequestBody>,
        @Part categoryList: List<MultipartBody.Part>,
        @Part multipartFile: MultipartBody.Part?,
    ): NetworkResponse<SignUpResponse>

    /** 소셜 회원가입 */
    @Multipart
    @POST("/login/signup/kakao")
    suspend fun kakoSignUp(
        @PartMap feedPostReqeust: HashMap<String, RequestBody>,
        @Part categoryList: List<MultipartBody.Part>,
        @Part multipartFile: MultipartBody.Part?,
    ): NetworkResponse<SignUpResponse>
}