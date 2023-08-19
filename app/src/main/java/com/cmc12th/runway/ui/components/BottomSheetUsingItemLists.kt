@file:OptIn(ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.theme.*
import kotlinx.coroutines.launch
import com.cmc12th.runway.ui.domain.model.BottomSheetState

@Composable
fun BottomSheetUsingItemLists(
    bottomsheetState: BottomSheetState,
    contents: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomsheetState.modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                if (bottomsheetState.bottomsheetContent.value.title.isNotBlank()) {
                    Text(
                        text = bottomsheetState.bottomsheetContent.value.title,
                        style = HeadLine4,
                        modifier = Modifier.padding(20.dp, 30.dp)
                    )
                }
                if (bottomsheetState.bottomsheetContent.value.title.isBlank()) {
                    HeightSpacer(height = 10.dp)
                }
                bottomsheetState.bottomsheetContent.value.itemList.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (it.isSeleceted) Primary20 else White)
                            .clickable {
                                it.onItemClick()
                                coroutineScope.launch {
                                    bottomsheetState.modalSheetState.hide()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(20.dp, 20.dp),
                            text = it.itemName,
                            style = Body1,
                            textAlign = TextAlign.Start,
                            color = it.itemTextColor
                        )
                        if (it.isSeleceted) {
                            Icon(
                                painter = painterResource(id = com.cmc12th.runway.R.drawable.ic_check),
                                contentDescription = "IC_CHECK",
                                tint = Primary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .offset(x = (-20).dp)
                            )
                        }
                    }
                }
                HeightSpacer(height = 20.dp)
            }
        }
    ) {
        contents()
    }
}

@Composable
fun CustomBottomSheet(
    bottomsheetState: BottomSheetState,
    contents: @Composable () -> Unit,
) {

    ModalBottomSheetLayout(
        sheetState = bottomsheetState.modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            bottomsheetState.contents.value()
        }
    ) {
        contents()
    }
}

