package com.cmc12th.domain.model

import com.cmc12th.runway.data.Constants.BASE_URL
import com.cmc12th.domain.model.ServiceInterceptor.Companion.accessToken
import com.cmc12th.runway.data.network.service.AuthService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

/**
 * OkHttp는 응답이 401 인증되지 않은 요청으로 마지막으로
 * 실패한 요청을 다시 시도할 때 Authenticator에게 자격 증명을 자동으로 요청한다.
 */
class TokenAuthenticator @Inject constructor() : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 200) return null
        return try {
            val newAccessToken = RefreshTokenService.refreshToken()
            response.request.newBuilder()
                .removeHeader("X-AUTH-TOKEN").apply {
                    addHeader("X-AUTH-TOKEN", newAccessToken)
                }.build()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

object RefreshTokenService {

    private val refreshRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
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
        val res = refreshService.tokenReissuance(ServiceInterceptor.refreshToken).execute()
        if (res.isSuccessful) {
            val newAccessToken = res.body()?.result?.accessToken ?: ""
            if (newAccessToken.isNotBlank()) {
                accessToken = newAccessToken
                return newAccessToken
            }
        }
        throw IllegalStateException("토큰 재발급 실패")
    }
}