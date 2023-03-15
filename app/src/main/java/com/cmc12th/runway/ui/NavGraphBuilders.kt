package com.cmc12th.runway.ui

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewDetailScreen
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewReportScreen
import com.cmc12th.runway.ui.detail.photoreview.view.ReviewWriteScreen
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.splash.SplashScreen
import com.cmc12th.runway.ui.webview.WebviewScreen
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_REPORT_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_WRITE_ROUTE
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE

fun NavGraphBuilder.detailComposable(appState: ApplicationState) {
    composable(
        route = "${DETAIL_ROUTE}?storeId={storeId}&storeName={storeName}",
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
}

fun NavGraphBuilder.splashComposeable(appState: ApplicationState) {
    composable(SPLASH_ROUTE) {
        SplashScreen(appState)
    }
}

fun NavGraphBuilder.webViewComposable(appState: ApplicationState) {
    composable(
        route = "${WEB_VIEW_ROUTE}?title={title}&url={url}",
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
}

fun NavGraphBuilder.reviewComposable(appState: ApplicationState) {
    composable(route = "${REVIEW_WRITE_ROUTE}?idx={idx}",
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
        route = "${REVIEW_DETAIL_ROUTE}?reviewId={reviewId}&viewerType={viewerType}",
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

    composable(route = "${REVIEW_REPORT_ROUTE}?reviewId={reviewId}",
        arguments = listOf(
            navArgument("reviewId") {
                type = NavType.IntType
            }
        )) { entry ->
        val idx = entry.arguments?.getInt("reviewId") ?: 0
        ReviewReportScreen(appState, idx)
    }
}
