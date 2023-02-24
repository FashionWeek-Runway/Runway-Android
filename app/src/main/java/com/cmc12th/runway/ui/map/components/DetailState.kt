package com.cmc12th.runway.ui.map.components

data class DetailState(
    var isVisible: Boolean,
    var id: Int,
    var storeName: String,
) {
    fun isDefault() = this == default()

    companion object {
        fun default() = DetailState(false, -1, "")
    }
}