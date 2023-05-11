package com.cmc12th.domain.model.response.user


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreMetaDataItem(
    @Json(name = "category")
    val category: List<String>,
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeImg")
    val storeImg: String?,
    @Json(name = "storeName")
    val storeName: String
)