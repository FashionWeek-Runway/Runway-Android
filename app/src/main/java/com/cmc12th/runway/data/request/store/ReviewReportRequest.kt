package com.cmc12th.runway.data.request.store

data class ReviewReportRequest(
    val opinion: String,
    val reason: Int,
    val reviewId: Int
)