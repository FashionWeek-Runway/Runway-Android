package com.cmc12th.runway.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus

@Composable
fun BoxScope.ReviewTextColorPicker(
    editStatus: EditUiStatus,
    updateEditUiStatus: (EditUiStatus) -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier
            .imePadding()
            .align(Alignment.BottomCenter)
            .padding(
                0.dp,
                20.dp
            ),
        enter = fadeIn(),
        exit = fadeOut(),
        visible = editStatus.isEdit && editStatus.isColorPickerVisibility
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                WidthSpacer(width = 0.dp)
            }
            items(EditUiStatus.REVIEW_FONT_COLORS) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(it)
                        .border(
                            BorderStroke(
                                if (editStatus.fontColor == it) 3.dp else 1.dp,
                                Color.White
                            ), CircleShape
                        )
                        .clickable {
                            updateEditUiStatus(
                                editStatus.copy(
                                    fontColor = it
                                )
                            )
                        }
                )
            }
            item {
                WidthSpacer(width = 0.dp)
            }
        }
    }
}
