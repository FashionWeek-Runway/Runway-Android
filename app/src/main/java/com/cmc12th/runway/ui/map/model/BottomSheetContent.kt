package com.cmc12th.runway.ui.map.model

import com.cmc12th.runway.data.response.map.MapInfoItem

sealed class BottomSheetContent(private val contents: List<MapInfoItem>) {
    object DEFAULT : BottomSheetContent(emptyList())
    object LOADING : BottomSheetContent(emptyList())
    class SINGLE(val storeName: String = "", contents: MapInfoItem) :
        BottomSheetContent(listOf(contents))

    class MULTI(val locationName: String = "", contents: List<MapInfoItem>) :
        BottomSheetContent(contents)

    fun getData() = this.contents
}