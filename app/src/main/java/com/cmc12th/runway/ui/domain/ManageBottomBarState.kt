package com.cmc12th.runway.ui.domain

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.utils.Constants

/** 바텀 네비게이션에 대한 Visibility를 관리한다. */
@Composable
fun ManageBottomBarState(
    navBackStackEntry: NavBackStackEntry?,
    applicationState: ApplicationState,
) {
    when (navBackStackEntry?.destination?.route) {
        Constants.HOME_ROUTE, Constants.MYPAGE_ROUTE -> applicationState.bottomBarState.value = true
        else -> applicationState.bottomBarState.value = false
    }
}