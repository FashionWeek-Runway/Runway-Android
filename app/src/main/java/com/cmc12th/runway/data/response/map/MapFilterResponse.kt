package com.cmc12th.runway.data.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapFilterResponse(
    @Json(name = "code")
    val code: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "result")
    val mapFilterItem: List<MapFilterItem>
)