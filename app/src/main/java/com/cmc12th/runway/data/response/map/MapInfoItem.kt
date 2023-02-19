package com.cmc12th.runway.data.response.map


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
    val storeName: String
)