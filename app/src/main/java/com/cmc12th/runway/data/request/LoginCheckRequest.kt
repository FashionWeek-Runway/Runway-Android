package com.cmc12th.runway.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginCheckRequest(
    @Json(name = "confirmNum")
    val confirmNum: String,
    @Json(name = "to")
    val to: String
)