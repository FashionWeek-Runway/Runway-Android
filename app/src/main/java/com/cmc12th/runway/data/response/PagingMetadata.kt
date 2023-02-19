package com.cmc12th.runway.data.response

import com.cmc12th.runway.data.response.map.MapInfoItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PagingMetadata<out T>(
    @Json(name = "contents")
    val contents: List<T>,
    @Json(name = "isLast")
    val isLast: Boolean
)