package com.cmc12th.runway.data.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlogReview(
    @Json(name = "content")
    val content: String,
    @Json(name = "imgCnt")
    val imgCnt: Int,
    @Json(name = "imgUrl")
    val imgUrl: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "webUrl")
    val webUrl: String
)