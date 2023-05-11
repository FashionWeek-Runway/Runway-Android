package com.cmc12th.domain.model.response.home

data class HomeReviewItem(
    val imgUrl: String,
    val read: Boolean,
    val regionInfo: String,
    val reviewId: Int
)