package com.cmc12th.runway.network

import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor(
    token: String? = null,
) : Interceptor {

    companion object {
        var authToken: String? = null
    }

    init {
        authToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null) {
            if (!authToken.isNullOrEmpty()) {
                val finalToken = "$authToken"
                request = request.newBuilder().apply {
                    addHeader("X-AUTH-TOKEN", finalToken)
                }.build()
            }
        }
        return chain.proceed(request)
    }
}