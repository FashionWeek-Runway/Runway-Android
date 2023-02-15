package com.cmc12th.runway.ui

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.detail.PhotoReviewResultScreen
import com.cmc12th.runway.ui.detail.photoreview.PhotoReviewScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.home.HomeScreen
import com.cmc12th.runway.ui.login.LoginIdPasswdScreen
import com.cmc12th.runway.ui.login.view.LoginBaseScreen
import com.cmc12th.runway.ui.map.view.MapScreen
import com.cmc12th.runway.ui.mypage.MypageScreen
import com.cmc12th.runway.ui.signin.view.*
import com.cmc12th.runway.utils.Constants.DETAIL_GRAPH
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.LOGIN_BASE_ROUTE
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_RESULT_ROUTE
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_ROUTE
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
        composable(Screen.Map.route) { MapScreen(appState) }
        composable(Screen.Mypage.route) { MypageScreen(appState) }
    }
}

fun NavGraphBuilder.detailGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = DETAIL_ROUTE, route = DETAIL_GRAPH) {
        composable(route = "$DETAIL_ROUTE?idx={idx}",
            arguments = listOf(
                navArgument("idx") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val idx = entry.arguments?.getInt("idx") ?: 0
            DetailScreen(appState, idx)
        }
        composable(PHOTO_REVIEW_ROUTE) {
            PhotoReviewScreen(appState)
        }
        composable(PHOTO_REVIEW_RESULT_ROUTE) {
            val userObject =
                appState.navController.previousBackStackEntry?.arguments?.getParcelable<Bitmap>("bitmap")
            PhotoReviewResultScreen(appState, userObject!!)
        }
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
        composable(
            route = "$SIGNIN_PROFILE_IMAGE_ROUTE?profileImage={profileImage}&kakaoId={kakaoId}",
            arguments = listOf(
                navArgument("profileImage") {
                    type = NavType.StringType
                },
                navArgument("kakaoId") {
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val profileImage = entry.arguments?.getString("profileImage") ?: ""
            val kakaoId = entry.arguments?.getString("kakaoId") ?: ""
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SIGNIN_GRAPH)
            SignInProfileImageScreen(
                appState = appState,
                signInViewModel = hiltViewModel(backStackEntry),
                profileImage = profileImage,
                kakaoId = kakaoId
            )
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
            SignInCompleteScreen(appState, hiltViewModel(backStackEntry))
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


