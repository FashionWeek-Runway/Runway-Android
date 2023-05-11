package com.cmc12th.domain.model.response.map

data class StoreInfo(
    val category: List<String>,
    val storeId: Int,
    val storeImg: String,
    val storeName: String
) {
    fun toMapInfoItem(): MapInfoItem = MapInfoItem(
        category = category,
        storeId = storeId,
        storeImg = storeImg,
        storeName = storeName
    )
}