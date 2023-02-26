package com.cmc12th.runway.ui.detail.photoreview.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus

@Composable
fun EditFocusTextField(
    updateEditUiState: (EditUiStatus) -> Unit,
    editUiState: EditUiStatus,
    textFieldWidth: Dp,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    BasicTextField(
        value = editUiState.textField,
        onValueChange = {
            updateEditUiState(
                editUiState.copy(
                    textField = it
                )
            )
        },
        textStyle = LocalTextStyle.current.copy(
            color = editUiState.fontColor,
            fontSize = editUiState.fontSize,
            lineHeight = editUiState.fontSize * 1.4f,
            fontFamily = FontFamily(Font(R.font.spoqa_han_sans_neo_medium)),
            textAlign = editUiState.textAlign
        ),
        modifier = Modifier
            .offset(x = 60.dp, y = 160.dp)
            .width(textFieldWidth)
            .wrapContentHeight()
            .focusRequester(focusRequester)
            .onFocusChanged {
            },
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                innerTextField()
            }
        },
    )
}