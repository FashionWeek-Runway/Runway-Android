package com.cmc12th.runway.ui.domain.model

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable

@OptIn(ExperimentalMaterialApi::class)
@Stable
class BottomSheetState(
    val modalSheetState: ModalBottomSheetState,
    val bottomsheetContent: MutableState<BottomSheetContent>,
)
