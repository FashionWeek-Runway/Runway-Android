package com.cmc12th.runway.ui.domain

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import com.cmc12th.runway.ui.domain.model.KeyboardStatus

@Composable
fun keyboardAsState(): State<KeyboardStatus> {
    val keyboardStatusState = remember { mutableStateOf(KeyboardStatus.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardStatusState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardStatus.Opened
            } else {
                KeyboardStatus.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardStatusState
}
