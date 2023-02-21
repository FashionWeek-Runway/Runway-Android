package com.cmc12th.runway.ui.map.model

import com.cmc12th.runway.data.response.map.MapInfoItem

sealed class BottomSheetContent(private val contents: List<MapInfoItem>) {
    object DEFAULT : BottomSheetContent(emptyList())
    object LOADING : BottomSheetContent(emptyList())
    class SINGLE(contents: MapInfoItem) : BottomSheetContent(listOf(contents))
    class MULTI(contents: List<MapInfoItem>) : BottomSheetContent(contents)

    fun getData() = this.contents
}