package com.cmc12th.domain.model.signin

data class CategoryTag(
    val id: Int,
    val name: String,
    val iconId: Int,
    var isSelected: Boolean = false,
) {
}