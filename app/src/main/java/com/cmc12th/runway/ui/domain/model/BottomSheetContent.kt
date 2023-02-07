package com.cmc12th.runway.ui.domain.model

data class BottomSheetContent(
    val title: String = "",
    val itemList: List<BottomSheetContentItem> = emptyList(),
)
