package com.cmc12th.domain.model.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapInfoItem(
    @Json(name = "category")
    val category: List<String>,
    @Json(name = "mainCategory")
    val mainCategory: String = "",
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeImg")
    val storeImg: String,
    @Json(name = "storeName")
    val storeName: String,
    @Json(name = "bookmark")
    val bookmark: Boolean = false,
    @Json(name = "latitude")
    val latitude: Double = 37.5665,
    @Json(name = "longitude")
    val longitude: Double = 126.9780,
)