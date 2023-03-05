package com.cmc12th.runway.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewWriteScreen
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewDetailScreen
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewReportScreen
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.home.view.HomeScreen
import com.cmc12th.runway.ui.login.view.LoginIdPasswdScreen
import com.cmc12th.runway.ui.login.passwordsearch.view.PasswordSearchChagnePasswordScreen
import com.cmc12th.runway.ui.login.passwordsearch.view.PasswordSearchPhoneScreen
import com.cmc12th.runway.ui.login.passwordsearch.view.PasswordSearchVerifyScreen
import com.cmc12th.runway.ui.login.view.LoginBaseScreen
import com.cmc12th.runway.ui.map.view.MapScreen
import com.cmc12th.runway.ui.mypage.view.EditProfileCompleteScreen
import com.cmc12th.runway.ui.mypage.view.EditProfileScreen
import com.cmc12th.runway.ui.mypage.view.MypageScreen
import com.cmc12th.runway.ui.setting.SettingMainScreen
import com.cmc12th.runway.ui.setting.view.SettingPersonalInfoManagementScreen
import com.cmc12th.runway.ui.setting.view.SettingWithdrawalScreen
import com.cmc12th.runway.ui.signin.view.*
import com.cmc12th.runway.ui.webview.WebviewScreen
import com.cmc12th.runway.utils.Constants.DETAIL_GRAPH
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.EDIT_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.LOGIN_BASE_ROUTE
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.LOGIN_ID_PW_ROUTE
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.MYPAGE_EDIT_PROFILE_COMPLETE_ROUTE
import com.cmc12th.runway.utils.Constants.MYPAGE_EDIT_PROFILE_ROUTE
import com.cmc12th.runway.utils.Constants.PASSWORD_SEARCH_GRAPH
import com.cmc12th.runway.utils.Constants.PASSWORD_SEARCH_PHONE_CHANGE_PASSWORD
import com.cmc12th.runway.utils.Constants.PASSWORD_SEARCH_PHONE_ROUTE
import com.cmc12th.runway.utils.Constants.PASSWORD_SEARCH_PHONE_VERIFY_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_REPORT_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_WRITE_ROUTE
import com.cmc12th.runway.utils.Constants.SETTING_GRAPH
import com.cmc12th.runway.utils.Constants.SETTING_MAIN_ROUTE
import com.cmc12th.runway.utils.Constants.SETTING_PERSONAL_INFO_MANAGEMENT_ROUTE
import com.cmc12th.runway.utils.Constants.SETTING_WITHDRAWAL_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_AGREEMENT_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_AGREEMENT_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_CATEGORY_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_COMPLETE_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH
import com.cmc12th.runway.utils.Constants.SIGNIN_PASSWORD_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PHONE_VERIFY_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_USER_VERIFICATION_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE


fun NavGraphBuilder.mainGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = Screen.Home.route, route = MAIN_GRAPH) {
        composable(Screen.Home.route) { HomeScreen(appState) }
        composable(Screen.Map.route) { MapScreen(appState) }
        composable(Screen.Mypage.route) {
            MypageScreen(appState)
        }

        composable(
            route = "$DETAIL_ROUTE?storeId={storeId}&storeName={storeName}",
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                },
                navArgument("storeName") {
                    type = NavType.StringType
                },
            )
        ) { entry ->
            val storeId = entry.arguments?.getInt("storeId") ?: -1
            val storeName = entry.arguments?.getString("storeName") ?: ""
            DetailScreen(
                appState = appState,
                idx = storeId,
                storeName = storeName,
                onBackPress = { appState.popBackStack() }
            )
        }

        composable(
            route = "$WEB_VIEW_ROUTE?title={title}&url={url}",
            arguments = listOf(
                navArgument("title") {
                    type = NavType.StringType
                },
                navArgument("url") {
                    type = NavType.StringType
                },
            )
        ) { entry ->
            val title = entry.arguments?.getString("title") ?: ""
            val url = entry.arguments?.getString("url") ?: ""
            WebviewScreen(appState, title, url)
        }

        composable(route = "$REVIEW_WRITE_ROUTE?idx={idx}",
            arguments = listOf(
                navArgument("idx") {
                    type = NavType.IntType
                }
            )) { entry ->
            val idx = entry.arguments?.getInt("idx") ?: 0
            val userObject =
                appState.navController.previousBackStackEntry?.arguments?.getParcelable<Uri>(
                    "uri"
                )
            ReviewWriteScreen(appState, idx, userObject)
        }

        composable(
            route = "$REVIEW_DETAIL_ROUTE?reviewId={reviewId}&viewerType={viewerType}",
            arguments = listOf(
                navArgument("reviewId") {
                    type = NavType.IntType
                },
                navArgument("viewerType") {
                    type = NavType.StringType
                },
            )
        ) { entry ->
            val idx = entry.arguments?.getInt("reviewId") ?: 0
            val viewerType =
                ReviewViwerType.convertStringToEnum(
                    entry.arguments?.getString("viewerType") ?: "STORE_DETAIL"
                )
            ReviewDetailScreen(appState, idx, viewerType)
        }

        composable(route = "$REVIEW_REPORT_ROUTE?reviewId={reviewId}",
            arguments = listOf(
                navArgument("reviewId") {
                    type = NavType.IntType
                }
            )) { entry ->
            val idx = entry.arguments?.getInt("reviewId") ?: 0
            ReviewReportScreen(appState, idx)
        }

    }
}

fun NavGraphBuilder.editProfileGraph(
    appState: ApplicationState
) {
    navigation(startDestination = MYPAGE_EDIT_PROFILE_ROUTE, route = EDIT_PROFILE_IMAGE_ROUTE) {
        composable(route = MYPAGE_EDIT_PROFILE_ROUTE) { entry ->
            val backStackEntry =
                rememberNavControllerBackEntry(entry, appState, MYPAGE_EDIT_PROFILE_ROUTE)
            EditProfileScreen(appState = appState, hiltViewModel(backStackEntry))
        }
        composable(route = MYPAGE_EDIT_PROFILE_COMPLETE_ROUTE) { entry ->
            val backStackEntry =
                rememberNavControllerBackEntry(entry, appState, MYPAGE_EDIT_PROFILE_ROUTE)
            EditProfileCompleteScreen(appState, hiltViewModel(backStackEntry))
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

fun NavGraphBuilder.passwordSearchGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = PASSWORD_SEARCH_PHONE_ROUTE, route = PASSWORD_SEARCH_GRAPH) {
        composable(PASSWORD_SEARCH_PHONE_ROUTE) { entry ->
            val backStackEntry =
                rememberNavControllerBackEntry(entry, appState, PASSWORD_SEARCH_GRAPH)
            PasswordSearchPhoneScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(PASSWORD_SEARCH_PHONE_VERIFY_ROUTE) { entry ->
            val backStackEntry =
                rememberNavControllerBackEntry(entry, appState, PASSWORD_SEARCH_GRAPH)
            PasswordSearchVerifyScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(PASSWORD_SEARCH_PHONE_CHANGE_PASSWORD) { entry ->
            val backStackEntry =
                rememberNavControllerBackEntry(entry, appState, PASSWORD_SEARCH_GRAPH)
            PasswordSearchChagnePasswordScreen(appState, hiltViewModel(backStackEntry))
        }

    }
}

fun NavGraphBuilder.settingGraph(
    appState: ApplicationState,
) {
    navigation(startDestination = SETTING_MAIN_ROUTE, route = SETTING_GRAPH) {
        composable(SETTING_MAIN_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SETTING_GRAPH)
            SettingMainScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SETTING_PERSONAL_INFO_MANAGEMENT_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SETTING_GRAPH)
            SettingPersonalInfoManagementScreen(appState, hiltViewModel(backStackEntry))
        }
        composable(SETTING_WITHDRAWAL_ROUTE) { entry ->
            val backStackEntry = rememberNavControllerBackEntry(entry, appState, SETTING_GRAPH)
            SettingWithdrawalScreen(appState, hiltViewModel(backStackEntry))
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


