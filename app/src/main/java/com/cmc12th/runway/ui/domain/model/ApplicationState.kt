package com.cmc12th.runway.ui.domain.model

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class ApplicationState(
    val bottomBarState: MutableState<Boolean>,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
) {
    suspend fun showSnackbar(message: String) {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}
