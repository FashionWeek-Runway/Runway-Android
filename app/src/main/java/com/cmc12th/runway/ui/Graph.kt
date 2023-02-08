package com.cmc12th.runway.ui

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
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
        composable(LOGIN_BASE_ROUTE) { LoginBaseScreen(appState) }
        composable(LOGIN_ID_PW_ROUTE) { LoginIdPasswdScreen(appState) }
    }
}

fun NavGraphBuilder.signInGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = SIGNIN_USER_VERIFICATION_ROUTE, route = SIGNIN_GRAPH) {
        composable(SIGNIN_USER_VERIFICATION_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            SignInUserInfoVerifyScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PHONE_VERIFY_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            SignInPhoneVerifyScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PASSWORD_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            SignInPasswordScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_AGREEMENT_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            SignInAgreementScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_PROFILE_IMAGE_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            SignInProfileImage(appState, hiltViewModel(backStackEntry))
        }
        composable(SIGNIN_AGREEMENT_DETAIL_ROUTE) { entry ->
            val backStackEntry = remember(entry) {
                appState.navController.getBackStackEntry(SIGNIN_GRAPH)
            }
            AgreementDetailScreen(appState, hiltViewModel(backStackEntry))
        }
//        composable(SIGNIN_CATEOGRY_ROUTE) {
//        }
    }
}


