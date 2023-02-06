package com.cmc12th.runway

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cmc12th.runway.ui.Screen
import com.cmc12th.runway.ui.components.BottomBar
import com.cmc12th.runway.ui.detail.PhotoReviewResultScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.login.signin.SignInProfileImage
import com.cmc12th.runway.ui.loginGraph
import com.cmc12th.runway.ui.mainGraph
import com.cmc12th.runway.ui.photoreview.PhotoReviewScreen
import com.cmc12th.runway.ui.signInGraph
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.ui.theme.RunwayTheme
import com.cmc12th.runway.utils.Constants.BOTTOM_NAV_ITEMS
import com.cmc12th.runway.utils.Constants.HOME_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAP_ROUTE
import com.cmc12th.runway.utils.Constants.MYPAGE_ROUTE
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROUTE
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RunwayTheme {
                val appState = rememberApplicationState()
                val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
                ManageBottomBarState(navBackStackEntry, appState)
                Surface(
                    modifier = if (appState.imePaddingState.value) Modifier
                        .statusBarsPadding()
                        .imePadding()
                        .navigationBarsPadding()
                        .fillMaxSize()
                    else Modifier
                        .statusBarsPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    SignInPhoneVerifyScreen()
//                    SignInProfileImage()
                    RootNavhost(appState)
                }
            }
        }
    }
}


@Composable
private fun rememberApplicationState(
    bottomBarState: MutableState<Boolean> = mutableStateOf(false),
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    imePaddingState: MutableState<Boolean> = mutableStateOf(false),
) = remember(bottomBarState, navController) {
    ApplicationState(
        bottomBarState,
        navController,
        scaffoldState,
        imePaddingState
    )
}

/** 바텀 네비게이션에 대한 Visibility를 관리한다. */
@Composable
private fun ManageBottomBarState(
    navBackStackEntry: NavBackStackEntry?,
    applicationState: ApplicationState,
) {
    when (navBackStackEntry?.destination?.route) {
        HOME_ROUTE, MAP_ROUTE, MYPAGE_ROUTE -> applicationState.bottomBarState.value = true
        else -> applicationState.bottomBarState.value = false
    }
    when (navBackStackEntry?.destination?.route) {
        PHOTO_REVIEW_ROUTE -> applicationState.imePaddingState.value = false
        else -> applicationState.imePaddingState.value = true
    }
}


/** NavHost를 정의하여 Navigation을 관리한다. */
@Composable
private fun RootNavhost(
    appState: ApplicationState,
) {
    Scaffold(
        scaffoldState = appState.scaffoldState,
        bottomBar = { BottomBar(appState) },
    ) { innerPadding ->
        NavHost(
            appState.navController,
            startDestination = SPLASH_ROUTE,
            Modifier
                .padding(innerPadding)
                .background(color = Color.White),
        ) {
            composable(SPLASH_ROUTE) {
                SplashScreen(appState)
            }
            composable(PHOTO_REVIEW_ROUTE) {
                PhotoReviewScreen(appState)
            }
            composable(PHOTO_REVIEW_RESULT_ROUTE) {
                val userObject =
                    appState.navController.previousBackStackEntry?.arguments?.getParcelable<Bitmap>(
                        "bitmap"
                    )
                PhotoReviewResultScreen(appState, userObject!!)
            }
            mainGraph(appState)
            loginGraph(appState)
            signInGraph(appState)
        }
    }
}