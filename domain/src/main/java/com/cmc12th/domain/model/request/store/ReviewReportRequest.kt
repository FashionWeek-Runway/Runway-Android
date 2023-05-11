package com.cmc12th.domain.model.request.store

data class ReviewReportRequest(
    val opinion: String,
    val reason: Int,
    val reviewId: Int
)