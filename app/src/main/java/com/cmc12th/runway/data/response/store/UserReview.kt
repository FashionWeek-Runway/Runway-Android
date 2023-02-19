package com.cmc12th.runway.data.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReview(
    @Json(name = "imgUrl")
    val imgUrl: String,
    @Json(name = "reviewId")
    val reviewId: Int
)