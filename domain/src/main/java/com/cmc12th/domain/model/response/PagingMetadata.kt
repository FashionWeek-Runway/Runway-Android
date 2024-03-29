package com.cmc12th.domain.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PagingMetadata<out T>(
    @Json(name = "contents")
    val contents: List<T> = emptyList(),
    @Json(name = "isLast")
    val isLast: Boolean,
)