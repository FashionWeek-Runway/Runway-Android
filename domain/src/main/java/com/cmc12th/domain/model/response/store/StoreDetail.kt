package com.cmc12th.domain.model.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreDetail(
    @Json(name = "address")
    val address: String = "",
    @Json(name = "bookmark")
    val bookmark: Boolean = false,
    @Json(name = "category")
    val category: List<String> = emptyList(),
    @Json(name = "imgUrlList")
    val imgUrlList: List<String> = emptyList(),
    @Json(name = "instagram")
    val instagram: String = "",
    @Json(name = "storeId")
    val storeId: Int = -1,
    @Json(name = "storeName")
    val storeName: String = "",
    @Json(name = "storePhone")
    val storePhone: String = "",
    @Json(name = "storeTime")
    val storeTime: String = "",
    @Json(name = "webSite")
    val webSite: String = "",
)