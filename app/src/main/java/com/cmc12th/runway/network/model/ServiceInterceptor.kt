package com.cmc12th.runway.network.model

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor : Interceptor {

    companion object {
        var accessToken: String? = null
        var refreshToken: String = ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null) {
            if (!accessToken.isNullOrEmpty()) {
                val finalToken = "$accessToken"
                request = request.newBuilder().apply {
                    addHeader("X-AUTH-TOKEN", finalToken)
                }.build()
            }
        }
        return chain.proceed(request)
    }
}