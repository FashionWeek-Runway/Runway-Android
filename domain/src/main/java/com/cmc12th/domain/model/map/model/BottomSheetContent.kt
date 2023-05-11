package com.cmc12th.domain.model.map.model

import androidx.paging.PagingData
import com.cmc12th.runway.data.response.map.MapInfoItem
import kotlinx.coroutines.flow.MutableStateFlow

sealed class BottomSheetContent() {
    object DEFAULT : BottomSheetContent()
    object LOADING : BottomSheetContent()
    class SINGLE(val storeName: String = "", val contents: MapInfoItem) :
        BottomSheetContent()

    class MULTI(
        val locationName: String = "",
        val contents: MutableStateFlow<PagingData<MapInfoItem>>,
    ) :
        BottomSheetContent()

}