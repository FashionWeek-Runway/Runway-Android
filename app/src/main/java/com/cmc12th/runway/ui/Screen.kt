package com.cmc12th.runway.ui

import androidx.annotation.DrawableRes
import com.cmc12th.runway.R
import com.cmc12th.runway.utils.Constants.HOME_ROUTE
import com.cmc12th.runway.utils.Constants.MAP_ROUTE
import com.cmc12th.runway.utils.Constants.MYPAGE_ROUTE

sealed class Screen(
    val route: String,
//    @StringRes val stringResId: Int,
    @DrawableRes val drawableResId: Int,
    @DrawableRes val selecteddrawableResId: Int
) {
    object Home :
        Screen(
            HOME_ROUTE,
            R.drawable.ic_nav_home_off,
            R.drawable.ic_nav_home_on,
        )

    object Map :
        Screen(
            MAP_ROUTE,
            R.drawable.ic_nav_map_off,
            R.drawable.ic_nav_map_on,
        )

    object Mypage :
        Screen(
            MYPAGE_ROUTE,
            R.drawable.ic_nav_mypage_off,
            R.drawable.ic_nav_mypage_on,
        )
}