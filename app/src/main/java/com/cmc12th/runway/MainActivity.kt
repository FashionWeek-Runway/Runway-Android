package com.cmc12th.runway

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cmc12th.runway.ui.domain.ManageBottomBarState
import com.cmc12th.runway.ui.domain.rememberApplicationState
import com.cmc12th.runway.ui.theme.RunwayTheme
import com.cmc12th.runway.utils.Constants.ANALYTICS_USER_PROP
import com.cmc12th.runway.utils.Constants.STATUS_INSTALLED
import com.cmc12th.runway.utils.Constants.STATUS_INSTANT
import com.google.android.gms.common.wrappers.InstantApps
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        if (InstantApps.isInstantApp(this)) {
            firebaseAnalytics?.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
        } else {
            firebaseAnalytics?.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
        }

        requestSMSReceivePermission()
        setContent {
            val appState = rememberApplicationState()
            val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
            RunwayTheme {
                ManageBottomBarState(navBackStackEntry, appState)
                appState.systmeUiController.setSystemBarsColor(color = Color.White)
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavhost(navBackStackEntry, appState)
                }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun requestSMSReceivePermission() {
        val permissions = arrayOf(Manifest.permission.RECEIVE_SMS)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

    companion object {
        var firebaseAnalytics: FirebaseAnalytics? = null
    }

}

