package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.NetworkResponse
import retrofit2.Call
import retrofit2.http.POST

interface AuthService {

    /** 로그아웃 */
    @POST("/user/logout")
    suspend fun logout(): DefaultResponse

    /** 유저 인증번호 전송 */
    @POST("/user/refresh")
    fun refreshToken(): Call<ResponseWrapper<OauthLoginRequest>>

}