package com.cmc12th.runway.ui.detail.photoreview.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.cmc12th.runway.ui.theme.Point
import com.cmc12th.runway.ui.theme.Primary

data class EditUiStatus(
    val isEdit: Boolean,
    val editIdx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textAlign: TextAlign = TextAlign.Start,
    val isColorPickerVisibility: Boolean = false,
    val textField: TextFieldValue = TextFieldValue(""),
) {
    companion object {

        val REVIEW_FONT_COLORS = listOf(
            Color.White,
            Color.Black,
            Primary,
            Point,
            Color(0xFFFBFF28),
            Color(0xFFFC3A56),
            Color(0xFFD700E7)
        )

        fun disabled() =
            EditUiStatus(
                false,
                -1,
                DEFAULT_REVIEW_FONT_SIZE,
                Color.White,
                TextAlign.Start,
                false,
                TextFieldValue("")
            )
    }
}