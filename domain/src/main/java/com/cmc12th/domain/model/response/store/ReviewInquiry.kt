package com.cmc12th.domain.model.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewInquiry(
    @Json(name = "nextReviewId")
    val nextReviewId: Int?,
    @Json(name = "prevReviewId")
    val prevReviewId: Int?
)