package com.cmc12th.runway.ui.signin.model

data class CategoryTag(
    val id: Int,
    val name: String,
    val iconId: Int,
    var isSelected: Boolean = false,
) {
}