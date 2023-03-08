package com.cmc12th.runway.utils

import com.cmc12th.runway.MainActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

fun viewLogEvent(screenName: String) {
    MainActivity.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param("screen_name", screenName)
    }
}
