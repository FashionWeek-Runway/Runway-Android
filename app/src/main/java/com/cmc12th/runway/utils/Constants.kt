package com.cmc12th.runway.utils

import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.Screen

object Constants {
    const val MAX_NICKNAME_LENGTH = 10
    val STATUSBAR_HEIGHT = 24.dp
    val BOTTOM_NAV_ITEMS = listOf<Screen>(Screen.Home, Screen.Map, Screen.Mypage)
    val CATEGORYS = listOf("미니멀", "캐주얼", "시티보이", "스트릿", "빈티지", "페미닌")
    const val SNS_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED"

    const val RUNWAY_DATASTORE = "runway-dataStore"

    /** navigation id */
    const val HOME_ROUTE = "nav-home"
    const val MAP_ROUTE = "nav-map"
    const val MYPAGE_ROUTE = "nav-mypage"

    const val LOGIN_BASE_ROUTE = "nav-login"
    const val LOGIN_ID_PW_ROUTE = "nav-login_id_pw"
    const val SPLASH_ROUTE = "nav-splash"

    const val SIGNIN_AGREEMENT_ROUTE = "nav-signin-agreement"
    const val SIGNIN_PHONE_VERIFY_ROUTE = "nav-signin-phone-verify"
    const val SIGNIN_USER_VERIFICATION_ROUTE = "nav-signin-user-verification"
    const val SIGNIN_PASSWORD_ROUTE = "nav-signin-password"
    const val SIGNIN_AGREEMENT_DETAIL_ROUTE = "nav-signin-agreement-detail"
    const val SIGNIN_PROFILE_IMAGE_ROUTE = "nav-signin-profile-image"
    const val SIGNIN_CATEGORY_ROUTE = "nav-signin-category"
    const val SIGNIN_COMPLETE_ROUTE = "nav-signin-complete"

    const val DETAIL_BASE_ROUTE = "nav-detail-base"
    const val PHOTO_REVIEW_ROUTE = "nav-photo-review"
    const val PHOTO_REVIEW_RESULT_ROUTE = "nav-photo-result-review"

    /** Graph Id */
    const val MAIN_GRAPH = "main-graph"
    const val LOGIN_GRAPH = "login-graph"
    const val SIGNIN_GRAPH = "signin-graph"
    const val DETAIL_GRAPH = "detail-graph"

}