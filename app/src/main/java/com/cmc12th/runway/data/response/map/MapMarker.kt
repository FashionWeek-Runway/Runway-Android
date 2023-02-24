package com.cmc12th.runway.data.response.map

import com.cmc12th.runway.ui.map.model.NaverItem
import com.naver.maps.geometry.LatLng

data class MapMarker(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val storeId: Int,
    val storeName: String
) {
    fun toSingleMarkerItem(): List<NaverItem> = listOf(
        NaverItem(
            position = LatLng(latitude, longitude),
            title = storeName,
            storeId = storeId,
        )
    )

    fun toMarkerItem(): NaverItem =
        NaverItem(
            position = LatLng(latitude, longitude),
            title = storeName,
            storeId = storeId,
        )
}