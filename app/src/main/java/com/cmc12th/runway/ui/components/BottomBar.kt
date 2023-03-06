package com.cmc12th.runway.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cmc12th.runway.ui.Screen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.utils.Constants

/** BottomNavigation Bar를 정의한다. */
@Composable
fun BoxScope.BottomBar(
    appState: ApplicationState,
    bottomNavItems: List<Screen> = Constants.BOTTOM_NAV_ITEMS,
) {

    AnimatedVisibility(
        visible = appState.bottomBarState.value,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(color = Color.Transparent)
            .navigationBarsPadding(),
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            bottomNavItems.forEachIndexed { _, screen ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                BottomNavigationItem(
                    icon = {
                        Surface {
                            Icon(
                                painter = painterResource(
                                    id =
                                    (if (isSelected) screen.selecteddrawableResId else screen.drawableResId),
                                ),
                                contentDescription = null,
                            )
                        }
                    },
                    label = null,
                    selected = isSelected,
                    onClick = {
                        appState.navController.navigate(screen.route) {
                            popUpTo(Constants.MAIN_GRAPH) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    selectedContentColor = Color.Unspecified,
                    unselectedContentColor = Color.Unspecified,
                )
            }
        }
    }
}