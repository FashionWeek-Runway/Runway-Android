package com.cmc12th.runway.ui.domain.model

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Stable
class ApplicationState(
    val bottomBarState: MutableState<Boolean>,
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
    val cameraPositionState: CameraPositionState,
    val systmeUiController: SystemUiController,
    private val coroutineScope: CoroutineScope,
) {

    fun changeBottomBarVisibility(visibility: Boolean) {
        bottomBarState.value = visibility
    }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message)
        }
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route)
    }
}
