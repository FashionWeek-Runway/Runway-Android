package com.cmc12th.runway.ui.detail.photoreview.model

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.detail.photoreview.EditUiStatus


data class UserReviewText(
    val idx: Int,
    val fontSize: TextUnit,
    val fontColor: Color,
    val textField: TextFieldValue = TextFieldValue(""),
    val textAlign: TextAlign = TextAlign.Start,
    val focusRequester: FocusRequester = FocusRequester(),
) {
    fun toEditUiStatus() = EditUiStatus(
        isEdit = true,
        editIdx = idx,
        fontSize = fontSize,
        textAlign = textAlign,
        fontColor = fontColor,
        textField = textField,
    )

    companion object {
        val DEFAULT_REVIEW_FONT_SIZE = 31.sp
    }
}