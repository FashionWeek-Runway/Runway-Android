package com.cmc12th.domain.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ResponseWrapper<out T>(
    @Json(name = "code")
    val code: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "result")
    val result: T
)