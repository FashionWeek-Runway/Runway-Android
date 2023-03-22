package com.cmc12th.runway.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.util.LastPasswordVisibleVisuualTransformation
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.theme.Error_Color
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray600

/**
 * 마지막 자리 비밀번호가 보이는 비밀번호 용 커스텀 텍스트 필드
 */
@Composable
fun LastPasswordVisibleCustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable() (() -> Unit)? = {},
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    value: String,
    onvalueChanged: (String) -> Unit,
    onErrorState: Boolean = false,
) {
    val hasFocus = remember {
        mutableStateOf(false)
    }
    val pswdVisible = remember {
        mutableStateOf(false)
    }
    val bottomLineColor = remember {
        mutableStateOf(Gray600)
    }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequest ?: FocusRequester())
            .onFocusChanged {
                hasFocus.value = it.isFocused
                if (it.isFocused) {
                    bottomLineColor.value = Color.Black
                } else {
                    bottomLineColor.value = Gray300
                }
            }
            .bottomBorder(1.dp, if (onErrorState) Error_Color else bottomLineColor.value),
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
                if (pswdVisible.value) {
                    Icon(
                        painter = painterResource(id = com.cmc12th.runway.R.drawable.ic_able_pw),
                        tint = Gray600,
                        contentDescription = "IC_ABLE_PW",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                pswdVisible.value = !pswdVisible.value
                            },
                    )
                } else {
                    Icon(
                        painter = painterResource(id = com.cmc12th.runway.R.drawable.ic_disable_pw),
                        tint = Color.Unspecified,
                        contentDescription = "IC_DISABLE_PW",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                pswdVisible.value = !pswdVisible.value
                            },
                    )
                }
            }
        },
        visualTransformation = if (pswdVisible.value) VisualTransformation.None else {
            LastPasswordVisibleVisuualTransformation(isFocused = hasFocus.value)
        }
    )

}