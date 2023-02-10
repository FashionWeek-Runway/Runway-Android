package com.cmc12th.runway.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "password")
    val password: String,
    @Json(name = "phone")
    val phone: String
)