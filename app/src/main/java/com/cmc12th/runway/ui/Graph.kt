package com.cmc12th.runway.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.home.HomeScreen
import com.cmc12th.runway.ui.login.LoginBaseScreen
import com.cmc12th.runway.ui.login.LoginIdPasswdScreen
import com.cmc12th.runway.ui.map.MapScreen
import com.cmc12th.runway.ui.mypage.MypageScreen
import com.cmc12th.runway.ui.signin.view.*
import com.cmc12th.runway.utils.Constants.LOGIN_BASE_ROUTE
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_AGREEMENT_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_AGREEMENT_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_CATEGORY_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_COMPLETE_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_PASSWORD_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PHONE_VERIFY_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_USER_VERIFICATION_ROUTE


fun NavGraphBuilder.mainGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = Screen.Home.route, route = MAIN_GRAPH) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Map.route) { MapScreen() }
        composable(Screen.Mypage.route) { MypageScreen(appState) }
    }
}

fun NavGraphBuilder.loginGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = LOGIN_BASE_ROUTE, route = LOGIN_GRAPH) {
        composable(LOGIN_BASE_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, LOGIN_GRAPH)
            LoginBaseScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(LOGIN_ID_PW_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, LOGIN_GRAPH)
            LoginIdPasswdScreen(appState, hiltViewModel(backStackEntry))
        }
    }
}

fun NavGraphBuilder.signInGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = SIGNIN_USER_VERIFICATION_ROUTE, route = SIGNIN_GRAPH) {
        composable(SIGNIN_USER_VERIFICATION_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInUserInfoVerifyScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PHONE_VERIFY_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInPhoneVerifyScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PASSWORD_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInPasswordScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_AGREEMENT_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInAgreementScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PROFILE_IMAGE_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInProfileImage(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_AGREEMENT_DETAIL_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            AgreementDetailScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_CATEGORY_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInCategoryScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_COMPLETE_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInCompleteScreen()
        }
    }
}

@Composable
private fun rememberNavControllerBackEntry(
    entry: NavBackStackEntry,
    appState: ApplicationState,
    graph: String,
) = remember(entry) {
    appState.navController.getBackStackEntry(graph)
}


