package com.cmc12th.runway

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cmc12th.runway.ui.*
import com.cmc12th.runway.ui.components.BottomBar
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.rememberApplicationState
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.ui.theme.RunwayTheme
import com.cmc12th.runway.utils.Constants.HOME_ROUTE
import com.cmc12th.runway.utils.Constants.MAP_ROUTE
import com.cmc12th.runway.utils.Constants.MYPAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSMSReceivePermission()
        setContent {
            val appState = rememberApplicationState()
            val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
            val systemUiController = rememberSystemUiController()
            RunwayTheme {
                systemUiController.setSystemBarsColor(color = Color.White)
                ManageBottomBarState(navBackStackEntry, appState)
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavhost(navBackStackEntry, appState)
                }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun requestSMSReceivePermission() {
        val permissions = arrayOf(Manifest.permission.RECEIVE_SMS)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

}


/** 바텀 네비게이션에 대한 Visibility를 관리한다. */
@Composable
private fun ManageBottomBarState(
    navBackStackEntry: NavBackStackEntry?,
    applicationState: ApplicationState,
) {
    when (navBackStackEntry?.destination?.route) {
        HOME_ROUTE, MYPAGE_ROUTE -> applicationState.bottomBarState.value = true
        else -> applicationState.bottomBarState.value = false
    }
}


/** NavHost를 정의하여 Navigation을 관리한다. */
@Composable
private fun RootNavhost(
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
        modifier = Modifier.customNavigationBarPaading(navBackStackEntry, appState),
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
                startDestination = SPLASH_ROUTE,
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = Color.White),
            ) {
                composable(SPLASH_ROUTE) {
                    SplashScreen(appState)
                }
                mainGraph(appState)
                loginGraph(appState)
                passwordSearchGraph(appState)
                signInGraph(appState)
                settingGraph(appState)
            }
            BottomBar(appState)
        }
    }
}

private fun Modifier.customNavigationBarPaading(
    navBackStackEntry: NavBackStackEntry?,
    appState: ApplicationState,
): Modifier {
    if (navBackStackEntry?.destination?.route == MAP_ROUTE) {
        return Modifier
    }
    if (appState.bottomBarState.value) {
        return Modifier
    }
    return Modifier.navigationBarsPadding()
}
