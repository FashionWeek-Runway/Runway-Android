package com.cmc12th.runway.ui.domain.model

data class BottomSheetContentItem(
    val itemName: String,
    val onItemClick: () -> Unit,
    val isSeleceted: Boolean = false,
)
