package com.cmc12th.runway.ui.detail.domain

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.lang.Float

@Composable
fun ManageSystemBarColor(
    scrollState: ScrollState,
    topbarColor: Color,
    updateTopbarColor: (Color) -> Unit,
    updateTopbarIconColor: (Color) -> Unit,
) {

    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(color = Color.Transparent)
        systemUiController.setNavigationBarColor(color = Color.White)
        onDispose {
            systemUiController.setSystemBarsColor(color = Color.White)
        }
    }
    LaunchedEffect(key1 = scrollState.value.dp) {
        if (scrollState.value.dp < 100.dp) {
            systemUiController.setSystemBarsColor(topbarColor)
            systemUiController.setNavigationBarColor(color = Color.White)
        } else systemUiController.setSystemBarsColor(Color.White)
    }

    LaunchedEffect(key1 = scrollState.value.dp) {
        if (scrollState.value.dp < 100.dp) {
            if (scrollState.value.dp < 50.dp) {
                updateTopbarColor(Color.Transparent)
            } else {
                updateTopbarColor(
                    Color.White.copy(
                        alpha = Float.min(
                            (scrollState.value.dp) / 100.dp,
                            100f
                        )
                    )
                )
            }
            updateTopbarIconColor(Color.White)
        } else {
            updateTopbarColor(Color.White)
            updateTopbarIconColor(Color.Black)
        }
    }
}
