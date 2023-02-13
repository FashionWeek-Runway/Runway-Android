package com.cmc12th.runway.network.model

import android.util.Log
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.di.NetworkModule.DEV_SERVER
import com.cmc12th.runway.network.model.ServiceInterceptor.Companion.accessToken
import com.cmc12th.runway.network.service.AuthService
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * OkHttp는 응답이 401 인증되지 않은 요청으로 마지막으로
 * 실패한 요청을 다시 시도할 때 Authenticator에게 자격 증명을 자동으로 요청한다.
 */
class TokenAuthenticator @Inject constructor() : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshTokenService = RefreshTokenService()
//        val newAccessToken = GlobalScope.async(Dispatchers.Default) {
//            refreshTokenService.refreshToken()
//        }
//        val token = runBlocking {
//            newAccessToken.await()
//        }
//        Log.i("AuthenticatorToken", token.toString())
        val newAccessToken = refreshTokenService.refreshToken()
        Log.i("Authenticator2", newAccessToken.toString())
        return response.request.newBuilder().apply {
            addHeader("X-AUTH-TOKEN", newAccessToken)
        }.build();
    }
}

class RefreshTokenService {

    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(DEV_SERVER)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val refreshService = refreshRetrofit.create(AuthService::class.java)

    fun refreshToken(): String {
        var newAccessToken = ""
        refreshService.tokenReissuance(ServiceInterceptor.refreshToken)
            .enqueue(object : retrofit2.Callback<ResponseWrapper<OauthLoginRequest>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<OauthLoginRequest>>,
                    response: retrofit2.Response<ResponseWrapper<OauthLoginRequest>>
                ) {
                    Log.i("Authenticator1", response.body().toString())
                    if (response.isSuccessful) {
                        newAccessToken = response.body()?.result?.accessToken ?: ""
                        accessToken = response.body()?.result?.accessToken ?: ""
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<OauthLoginRequest>>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }
            })
        return newAccessToken
    }
}