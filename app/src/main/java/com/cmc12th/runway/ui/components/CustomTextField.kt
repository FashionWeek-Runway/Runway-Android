package com.cmc12th.runway.ui.components

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.signin.view.ErrorMessage
import com.cmc12th.runway.ui.theme.Error_Color
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray600

@Composable
fun CustomTextField(
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
    onErrorState: Boolean = false,
    errorMessage: String = "",
) {
    val bottomLineColor = remember {
        mutableStateOf(Gray600)
    }
    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .bottomBorder(1.dp, Gray300)
                .onFocusChanged {
                    onFocuseChange(it.isFocused)
                    if (it.isFocused) {
                        bottomLineColor.value = Color.Black
                    } else {
                        bottomLineColor.value = Gray600
                    }
                }
                .bottomBorder(1.dp, if (onErrorState) Error_Color else bottomLineColor.value)
                .focusRequester(focusRequest ?: FocusRequester()),
            value = value,
            onValueChange = {
                if (it.text.length <= 25) onvalueChanged(it)
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
            visualTransformation = VisualTransformation.None,
        )
        if (onErrorState) ErrorMessage(message = errorMessage)
    }
}


/**
 * @param onErrorState 해당 값이 true라면 밑줄이 색이 빨개지고, 에러 메시지를 출력한다.
 * @param onFocuseChange 포커스 값을 외부에서 관찰할 때 사용
 */
@Composable
fun CustomTextField(
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
    onErrorState: Boolean = false,
    errorMessage: String = "",
) {
    val bottomLineColor = remember {
        mutableStateOf(Gray600)
    }
    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .bottomBorder(1.dp, Gray300)
                .onFocusChanged {
                    onFocuseChange(it.isFocused)
                    if (it.isFocused) {
                        bottomLineColor.value = Color.Black
                    } else {
                        bottomLineColor.value = Gray600
                    }
                }
                .bottomBorder(1.dp, if (onErrorState) Error_Color else bottomLineColor.value)
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
            visualTransformation = VisualTransformation.None,
        )
        if (onErrorState) ErrorMessage(message = errorMessage)
    }
}
