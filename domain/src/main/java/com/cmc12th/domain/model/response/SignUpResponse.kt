package com.cmc12th.domain.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpResponse(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "categoryList")
    val categoryList: List<String>,
    @Json(name = "imgUrl")
    val imgUrl: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "refreshToken")
    val refreshToken: String,
    @Json(name = "userId")
    val userId: Int
)