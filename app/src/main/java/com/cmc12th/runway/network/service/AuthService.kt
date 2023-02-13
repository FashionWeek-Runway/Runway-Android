package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.NetworkResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    /** 로그아웃 */
    @POST("/user/logout")
    suspend fun logout(): DefaultResponse

    /** 리프레쉬 토큰 유효성 검증  */
    @POST("/login/refresh")
    suspend fun loginRefresh(@Header("X-REFRESH-TOKEN") refreshToken: String): NetworkResponse<LoginResponse>

}