package com.cmc12th.runway.network.model

import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.di.NetworkModule
import com.cmc12th.runway.di.NetworkModule.DEV_SERVER
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.service.AuthService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

/**
 * OkHttp는 응답이 401 인증되지 않은 요청으로 마지막으로
 * 실패한 요청을 다시 시도할 때 Authenticator에게 자격 증명을 자동으로 요청한다.
 */
class TokenAuthenticator @Inject constructor(
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshTokenService = RefreshTokenService()
        val newAccessToken = refreshTokenService.refreshToken()
        return response.request.newBuilder().apply {
            addHeader("X-AUTH-TOKEN", newAccessToken)
        }.build();
    }
}

class RefreshTokenService {
    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(DEV_SERVER)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val refreshService = refreshRetrofit.create(AuthService::class.java)

    fun refreshToken(): String {
        var accessToken = ""
        refreshService.refreshToken()
            .enqueue(object : Callback<ResponseWrapper<OauthLoginRequest>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<OauthLoginRequest>>,
                    response: retrofit2.Response<ResponseWrapper<OauthLoginRequest>>,
                ) {
                    if (response.isSuccessful) {
                        accessToken = response.body()?.result?.accessToken ?: ""
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<OauthLoginRequest>>,
                    t: Throwable,
                ) {
                    t.printStackTrace()
                }
            })
        return accessToken
    }
}