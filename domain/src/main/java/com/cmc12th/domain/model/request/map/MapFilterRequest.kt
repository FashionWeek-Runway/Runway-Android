package com.cmc12th.domain.model.request.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapFilterRequest(
    @Json(name = "category")
    val category: List<String>,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double
)