package com.cmc12th.runway.data.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReviewDetail(
    @Json(name = "imgUrl")
    val imgUrl: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "profileImgUrl")
    val profileImgUrl: String,
    @Json(name = "regionInfo")
    val regionInfo: String,
    @Json(name = "reviewId")
    val reviewId: Int,
    @Json(name = "reviewInquiry")
    val reviewInquiry: ReviewInquiry,
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeName")
    val storeName: String
)