package com.cmc12th.runway.network.service

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.SignUpResponse
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoreService {

    /** 쇼룸 홈화면 조회 */
    @GET("/stores")
    suspend fun stores(): DefaultResponse

}