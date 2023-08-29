package com.cmc12th.runway.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun moveToNaverMap(
    context: Context,
    latitude: Double,
    longitude: Double,
    storeName: String,
) {
    val naverMapPackage = "com.nhn.android.nmap"
    val packageManager = context.packageManager
    val isNaverMapInstalled = isPackageInstalled(naverMapPackage, packageManager)
    val isKakaoMapInstalled = isPackageInstalled("net.daum.android.map", packageManager)

    // 네이버 맵 URL 스킴 : https://guide-gov.ncloud-docs.com/docs/naveropenapiv3-maps-url-scheme-url-scheme
    // 카카오 맵 URL 스킴 : https://apis.map.kakao.com/android/guide/#urlscheme
    if (isNaverMapInstalled) {
        val url =
            "nmap://search?lat=${latitude}&lng=${longitude}&query=${storeName}&appname=com.cmc12th.runway"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } else {
        if (isKakaoMapInstalled) {
            val url =
                "daummaps://search?q=${storeName}&p=${latitude},${longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.nhn.android.nmap")
            )
        )
    }
}
