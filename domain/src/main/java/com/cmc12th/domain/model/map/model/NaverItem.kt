package com.cmc12th.domain.model.map.model

import com.naver.maps.geometry.LatLng

data class NaverItem(
    var position: LatLng,
    var title: String,
    val storeId: Int,
    var description: String = "",
    var bookmark: Boolean = false,
    var isClicked: Boolean = false,
) {

    fun getTedLatLng(): LatLng {
        return LatLng(position.latitude, position.longitude)
    }

    companion object {
        fun default(): NaverItem = NaverItem(
            LatLng(0.0, 0.0), "", -999, ""
        )
    }
}