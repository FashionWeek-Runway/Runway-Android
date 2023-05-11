package com.cmc12th.domain.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "refreshToken")
    val refreshToken: String,
    @Json(name = "userId")
    val userId: Int
)