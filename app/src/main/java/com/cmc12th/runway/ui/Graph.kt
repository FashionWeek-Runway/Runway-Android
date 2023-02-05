package com.cmc12th.runway.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.home.HomeScreen
import com.cmc12th.runway.ui.map.MapScreen
import com.cmc12th.runway.ui.mypage.MypageScreen
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH


fun NavGraphBuilder.mainGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = Screen.Home.route, route = MAIN_GRAPH) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Map.route) { MapScreen() }
        composable(Screen.Mypage.route) { MypageScreen(appState) }
    }
}