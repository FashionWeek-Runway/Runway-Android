package com.cmc12th.runway.ui.map.model

import com.naver.maps.geometry.LatLng
import com.squareup.moshi.Json
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

data class NaverItem(
    var position: LatLng,
    var title: String,
    val storeId: Int,
    var description: String = "",
    var bookmark: Boolean = false,
    var isClicked: Boolean = false,
) : TedClusterItem {

    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(position.latitude, position.longitude)
    }

    companion object {
        fun default(): NaverItem = NaverItem(
            LatLng(0.0, 0.0), "", -999, ""
        )
    }
}