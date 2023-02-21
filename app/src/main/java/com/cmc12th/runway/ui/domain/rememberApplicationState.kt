package com.cmc12th.runway.ui.domain

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberApplicationState(
    bottomBarState: MutableState<Boolean> = mutableStateOf(false),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
//        position = CameraPosition(LatLng(37.5437, 127.0659), 8.0)
    },
) = remember(Unit) {
    ApplicationState(
        bottomBarState,
        navController,
        scaffoldState,
        cameraPositionState,
        coroutineScope,
    )
}