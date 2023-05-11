package com.cmc12th.domain.model.response.user


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyPageInfo(
    @Json(name = "imgUrl")
    val imgUrl: String?,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "ownerCheck")
    val ownerCheck: Boolean
)