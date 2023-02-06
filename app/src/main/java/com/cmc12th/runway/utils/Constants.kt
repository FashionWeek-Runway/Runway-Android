package com.cmc12th.runway.utils

import com.cmc12th.runway.ui.Screen

object Constants {
    val BOTTOM_NAV_ITEMS = listOf<Screen>(Screen.Home, Screen.Map, Screen.Mypage)

    /** navigation id */
    // HomeNavigation
    const val HOME_ROUTE = "nav-home"
    const val MAP_ROUTE = "nav-map"
    const val MYPAGE_ROUTE = "nav-mypage"

    const val PHOTO_REVIEW_ROUTE = "nav-photo-review"
    const val PHOTO_REVIEW_RESULT_ROUTE = "nav-photo-result-review"

    const val LOGIN_BASE_ROUTE = "nav-login"
    const val LOGIN_ID_PW_ROUTE = "nav-login_id_pw"
    const val SPLASH_ROUTE = "nav-splash"

    const val SIGNIN_AGREEMENT_ROUTE = "nav-signin-agreement"
    const val SIGNIN_PHONE_VERIFY_ROUTE = "nav-signin-phone-verify"
    const val SIGNIN_USER_VERIFICATION_ROUTE = "nav-signin-user-verification"
    const val SIGNIN_PASSWORD_ROUTE = "nav-signin-password"
    const val SIGNIN_PROFILE_IMAGE_ROUTE = "nav-signin-profile-image"
    const val SIGNIN_AGREEMENT_DETAIL_ROUTE = "nav-signin-agreement-detail"

    /** Graph Id */
    const val MAIN_GRAPH = "main-graph"
    const val LOGIN_GRAPH = "login-graph"
    const val SIGNIN_GRAPH = "signin-graph"
}