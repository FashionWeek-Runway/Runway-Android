package com.cmc12th.runway.data.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreDetail(
    @Json(name = "address")
    val address: String,
    @Json(name = "bookmark")
    val bookmark: Boolean,
    @Json(name = "category")
    val category: String,
    @Json(name = "imgUrlList")
    val imgUrlList: String,
    @Json(name = "instagram")
    val instagram: String,
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeName")
    val storeName: String,
    @Json(name = "storePhone")
    val storePhone: String,
    @Json(name = "storeTime")
    val storeTime: String,
    @Json(name = "webSite")
    val webSite: String
)