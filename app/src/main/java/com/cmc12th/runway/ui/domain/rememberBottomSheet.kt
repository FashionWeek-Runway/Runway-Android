@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.domain

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetState

@Composable
fun rememberBottomSheet(
    modalSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    ),
    bottomsheetContent: MutableState<BottomSheetContent> = mutableStateOf(BottomSheetContent()),
    contents: MutableState<@Composable () -> Unit> = mutableStateOf({}),
): BottomSheetState = remember(modalSheetState, bottomsheetContent) {
    BottomSheetState(
        modalSheetState,
        bottomsheetContent,
        contents
    )
}


