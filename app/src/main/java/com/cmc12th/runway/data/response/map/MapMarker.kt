package com.cmc12th.runway.data.response.map

data class MapMarker(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val storeId: Int,
    val storeName: String
)