package com.cmc12th.runway.data.response.user


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyReviewsItem(
    @Json(name = "imgUrl")
    val imgUrl: String?,
    @Json(name = "regionInfo")
    val regionInfo: String,
    @Json(name = "reviewId")
    val reviewId: Int
)