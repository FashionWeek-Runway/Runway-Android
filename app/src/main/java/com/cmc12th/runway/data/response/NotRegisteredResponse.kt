package com.cmc12th.runway.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotRegisteredResponse(
    @Json(name = "kakaoId")
    val kakaoId: String,
    @Json(name = "profileImgUrl")
    val profileImgUrl: String
)