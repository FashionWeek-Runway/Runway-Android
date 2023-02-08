package com.cmc12th.runway.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray600

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    value: TextFieldValue,
    onvalueChanged: (TextFieldValue) -> Unit,
    passwordVisible: Boolean = true
) {
    val bottomLineColor = remember {
        mutableStateOf(Gray600)
    }
    BasicTextField(
        modifier = modifier
            .bottomBorder(1.dp, Gray300)
            .onFocusChanged {
                if (it.isFocused) {
                    bottomLineColor.value = Color.Black
                } else {
                    bottomLineColor.value = Gray600
                }
            }
            .bottomBorder(1.dp, bottomLineColor.value)
            .focusRequester(focusRequest ?: FocusRequester()),
        value = value,
        onValueChange = {
            if (it.selection.length <= 25) onvalueChanged(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = fontSize,
        ),
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
        visualTransformation =
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    value: String,
    onvalueChanged: (String) -> Unit,
    passwordVisible: Boolean = true
) {
    val bottomLineColor = remember {
        mutableStateOf(Gray600)
    }
    BasicTextField(
        modifier = modifier
            .bottomBorder(1.dp, Gray300)
            .onFocusChanged {
                if (it.isFocused) {
                    bottomLineColor.value = Color.Black
                } else {
                    bottomLineColor.value = Gray600
                }
            }
            .bottomBorder(1.dp, bottomLineColor.value)
            .focusRequester(focusRequest ?: FocusRequester()),
        value = value,
        onValueChange = {
            if (it.length <= 25) onvalueChanged(it)
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = fontSize,
        ),
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
        visualTransformation =
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}