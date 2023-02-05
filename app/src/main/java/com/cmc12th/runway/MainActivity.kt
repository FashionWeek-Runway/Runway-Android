package com.cmc12th.runway

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
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
import com.cmc12th.runway.ui.detail.PhotoReviewResultScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.mainGraph
import com.cmc12th.runway.ui.photoreview.PhotoReviewScreen
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.ui.theme.Gray600
import com.cmc12th.runway.ui.theme.RunwayTheme
import com.cmc12th.runway.utils.Constants.BOTTOM_NAV_ITEMS
import com.cmc12th.runway.utils.Constants.HOME_ROOT
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAP_ROOT
import com.cmc12th.runway.utils.Constants.MYPAGE_ROOT
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROOT
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_ROOT
import com.cmc12th.runway.utils.Constants.SPLASH

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RunwayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootIndex()
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
) = remember(bottomBarState, navController) {
    ApplicationState(
        bottomBarState,
        navController,
        scaffoldState,
    )
}

/** 바텀 네비게이션에 대한 Visibility를 관리한다. */
@Composable
private fun ManageBottomBarState(
    navBackStackEntry: NavBackStackEntry?,
    bottomBarState: MutableState<Boolean>,
) {
    when (navBackStackEntry?.destination?.route) {
        HOME_ROOT, MAP_ROOT, MYPAGE_ROOT -> bottomBarState.value = true
        else -> bottomBarState.value = false
    }
}


/** State값들을 정의한 Composable */
@Composable
private fun RootIndex() {
    val appState = rememberApplicationState()
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    ManageBottomBarState(navBackStackEntry, appState.bottomBarState)
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        RootNavhost(appState)
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
            startDestination = SPLASH,
            Modifier
                .padding(innerPadding)
                .background(color = Color.White),
        ) {
            composable(SPLASH) {
                SplashScreen(appState)
            }
            composable(PHOTO_REVIEW_ROOT) {
                PhotoReviewScreen(appState)
            }
            composable(PHOTO_REVIEW_RESULT_ROOT) {
                val userObject =
                    appState.navController.previousBackStackEntry?.arguments?.getParcelable<Bitmap>(
                        "bitmap"
                    )
                PhotoReviewResultScreen(appState, userObject!!)
            }
            mainGraph(appState)
        }
    }
}


/** BottomNavigation Bar를 정의한다. */
@Composable
private fun BottomBar(
    appState: ApplicationState,
    bottomNavItems: List<Screen> = BOTTOM_NAV_ITEMS,
) {
    AnimatedVisibility(
        visible = appState.bottomBarState.value,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = Modifier.background(color = Color.White),
    ) {
        BottomNavigation(
//            modifier = Modifier
//                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            backgroundColor = Color.White,
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
                    /** 타이틀 달것이면 여기 */
//                    if (isSelected) {
//                        { Text(text = stringResource(screen.stringResId), color = Color.White) }
//                    } else {
//                        null
//                    },
                    selected = isSelected,
                    onClick = {
                        appState.navController.navigate(screen.route) {
                            popUpTo(MAIN_GRAPH) {
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