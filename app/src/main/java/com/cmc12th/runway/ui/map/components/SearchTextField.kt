package com.cmc12th.runway.ui.map.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.theme.Body1M

@Composable
fun SearchTextField(
    value: String,
    onvalueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    onFocuseChange: (Boolean) -> Unit = {},
) {
    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                onFocuseChange(it.isFocused)
            }
            .focusRequester(focusRequest ?: FocusRequester()),
        value = value,
        onValueChange = {
            if (it.length <= 25) onvalueChanged(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = Body1M,
        keyboardOptions = keyboardOptions ?: KeyboardOptions(),
        keyboardActions = keyboardActions ?: KeyboardActions(),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 15.dp)
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = Color.Black.copy(alpha = 0.3f),
                                fontSize = fontSize,
                            ),
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        },
        visualTransformation = VisualTransformation.None,
    )
}


@Composable
fun SearchTextField(
    value: TextFieldValue,
    onvalueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    onFocuseChange: (Boolean) -> Unit = {},
) {
    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                onFocuseChange(it.isFocused)
            }
            .focusRequester(focusRequest ?: FocusRequester()),
        value = value,
        onValueChange = {
            if (it.text.length <= 25) onvalueChanged(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = Body1M,
        keyboardOptions = keyboardOptions ?: KeyboardOptions(),
        keyboardActions = keyboardActions ?: KeyboardActions(),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(0.dp, 15.dp)
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.text.isEmpty()) {
                        Text(
                            placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = Color.Black.copy(alpha = 0.3f),
                                fontSize = fontSize,
                            ),
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        },
        visualTransformation = VisualTransformation.None,
    )
}
