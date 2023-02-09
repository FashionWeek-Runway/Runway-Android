package com.cmc12th.runway.ui.components.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class LastPasswordVisibleVisuualTransformation(
    private val mask: Char = '\u2022',
    private val isFocused: Boolean,
) :
    VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = if (isFocused) {
                if (text.text.isNotEmpty()) {
                    AnnotatedString(mask.toString().repeat(text.text.length - 1) + text.text.last())
                } else {
                    AnnotatedString(mask.toString().repeat(text.text.length))
                }
            } else {
                AnnotatedString(mask.toString().repeat(text.text.length))
            },
            offsetMapping = OffsetMapping.Identity
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PasswordVisualTransformation) return false
        if (mask != other.mask) return false
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}
