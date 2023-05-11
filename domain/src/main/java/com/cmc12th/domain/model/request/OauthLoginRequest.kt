package com.cmc12th.domain.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OauthLoginRequest(
    @Json(name = "accessToken")
    val accessToken: String
)