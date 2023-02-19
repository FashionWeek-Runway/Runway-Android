package com.cmc12th.runway.data.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilteringMetadata(
    @Json(name = "contents")
    val mapInfoItems: List<MapInfoItem>,
    @Json(name = "isLast")
    val isLast: Boolean
)