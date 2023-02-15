package com.cmc12th.runway.ui.domain.model

import androidx.compose.runtime.Composable

data class DialogButtonContent(
    val title: String,
    val onClick: () -> Unit,
) {
    @Composable
    fun isEnable(content: @Composable (DialogButtonContent) -> Unit) {
        if (title.isNotBlank()) {
            content(this)
        }
    }

    companion object {
        fun default(): DialogButtonContent {
            return DialogButtonContent("") {}
        }
    }
}