package com.cmc12th.runway.ui.domain.model

import androidx.compose.ui.graphics.Color

data class BottomSheetContentItem(
    val itemName: String,
    val itemTextColor: Color = Color.Black,
    val onItemClick: () -> Unit,
    val isSeleceted: Boolean = false,
)
