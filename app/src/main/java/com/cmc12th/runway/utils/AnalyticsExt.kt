package com.cmc12th.runway.utils

import com.cmc12th.runway.MainActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent


const val LOOKUP_EVENT = "lookup_event"
const val SCREEN_NAME = "screen_name"
const val TOUCH_NAME = "touch_name"
const val HOME_TOUCH_EVENT = "home_touch_event"
const val MAP_TOUCH_EVENT = "map_touch_event"
const val MYPAGE_TOUCH_EVENT = "mypage_touch_event"

fun lookupLogEvent(screenName: String) {
    MainActivity.firebaseAnalytics?.logEvent(LOOKUP_EVENT) {
        param(SCREEN_NAME, screenName)
    }
}

fun clickLogEvent(eventName: String, value: String) {
    // 클릭 이벤트를 파이어베이스에 남긴다.
    MainActivity.firebaseAnalytics?.logEvent(eventName) {
        param(TOUCH_NAME, value)
    }
}