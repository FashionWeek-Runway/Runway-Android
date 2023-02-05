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
    

    /** Graph Id */
    const val MAIN_GRAPH = "main-graph"
    const val LOGIN_GRAPH = "login-graph"
    const val SIGNIN_GRAPH = "signin-graph"
}