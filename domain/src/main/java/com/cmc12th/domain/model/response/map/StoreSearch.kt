package com.cmc12th.domain.model.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreSearch(
    @Json(name = "address")
    val address: String,
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeName")
    val storeName: String
)