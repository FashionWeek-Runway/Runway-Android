package com.cmc12th.runway.data.response.map


import com.cmc12th.runway.ui.map.model.NaverItem
import com.naver.maps.geometry.LatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapFilterItem(
    @Json(name = "bookmark")
    val bookmark: Boolean,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "mainCategory")
    val mainCategory: String = "",
    @Json(name = "storeId")
    val storeId: Int,
    @Json(name = "storeName")
    val storeName: String
)

fun List<MapFilterItem>.toNaverMapItem(): List<NaverItem> = this.map {
    NaverItem(
        title = it.storeName,
        position = LatLng(it.latitude, it.longitude),
        storeId = it.storeId,
    )
}