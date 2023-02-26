package com.cmc12th.runway.ui.detail.photoreview.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


@Composable
fun BoxScope.FontSizeToolBar(
    editUiState: EditUiStatus,
    updateEditUiState: (EditUiStatus) -> Unit,
) {
    var offsetY by remember { mutableStateOf(0.dp.value) }
    LaunchedEffect(key1 = Unit) {
        offsetY = (editUiState.fontSize.value - 31.sp.value) * 19f * -1
    }
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = 20.dp, y = 100.dp)
            .height(240.dp)
            .width(24.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(path = Path().apply {
                moveTo(size.width / 2f, size.height)
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }, color = Color(0x50EEF0F3))
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(0, y = offsetY.roundToInt())
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val dy = offsetY + delta
                        val my = if (dy >= 0.dp.value) min(
                            300.dp.value,
                            dy
                        ) else max(-300.dp.value, dy)

                        updateEditUiState(
                            editUiState.copy(
                                fontSize = (31.sp.value + (my / 19f * -1).sp.value).sp,
                                textField = editUiState.textField
                            )
                        )

                        offsetY = my
                    }
                ),
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                    ) { change, dragAmount ->
//                        change.consume()
//                        val dy = offsetY + dragAmount.y
//                        val my = if (dy >= 0.dp.value) kotlin.math.min(250.dp.value,
//                            dy) else kotlin.math.max(-250.dp.value, dy)
//                        updateEditUiState(editUiState.copy(
//                            fontSize = (((my + 250.dp.value) / 5.dp.value) * 38.sp.value + 12.sp.value).sp,
//                            textField = editUiState.textField
//                        ))
//                        offsetY = my
//                    }
//                }
        ) {
            drawCircle(
                color = Color.White,
                radius = size.width / 2,
            )
        }
    }
}