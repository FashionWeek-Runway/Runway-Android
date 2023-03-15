package com.cmc12th.runway

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cmc12th.runway.ui.*
import com.cmc12th.runway.ui.components.BottomBar
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.utils.Constants


@Composable
fun RootNavhost(
    navBackStackEntry: NavBackStackEntry?,
    appState: ApplicationState,
) {
    Scaffold(
        scaffoldState = appState.scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = appState.scaffoldState.snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.padding(
                            bottom = 50.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                    ) {
                        Text(text = data.message)
                    }
                }
            )
        },
        modifier = customNavigationBarPaading(navBackStackEntry, appState),
        bottomBar = {
//            if (appState.bottomBarState.value) BottomBar(appState)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                appState.navController,
                startDestination = Constants.SPLASH_ROUTE,
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = Color.White),
            ) {
                splashComposeable(appState)
                mainGraph(appState)
                loginGraph(appState)
                editProfileGraph(appState)
                passwordSearchGraph(appState)
                signInGraph(appState)
                settingGraph(appState)

                detailComposable(appState)
                webViewComposable(appState)
                reviewComposable(appState)
            }
            BottomBar(appState)
        }
    }
}


private fun customNavigationBarPaading(
    navBackStackEntry: NavBackStackEntry?,
    appState: ApplicationState,
): Modifier {
    if (navBackStackEntry?.destination?.route == Constants.MAP_ROUTE) {
        return Modifier
    }
    if (appState.bottomBarState.value) {
        return Modifier
    }
    return Modifier.navigationBarsPadding()
}
