package com.cmc12th.runway.ui.webview

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1B
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState


@Composable
fun WebviewScreen(appState: ApplicationState, title: String, url: String) {
    val state = rememberWebViewState(url)
    Log.i("dlgocks1", url)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.navController.popBackStack()
            }
            Text(text = title, style = Body1B, color = Black)
        }

        WebView(state = state,
            modifier = Modifier.weight(1f),
            onCreated = {
                it.settings.javaScriptEnabled = true
//                it.webViewClient = object : WebViewClient() {
//                    override fun shouldOverrideUrlLoading(
//                        view: WebView?,
//                        request: WebResourceRequest?
//                    ): Boolean {
//                        return if (url == null || url.startsWith("http://") || url.startsWith("https://")) false
//                        else try {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                            view!!.context.startActivity(intent)
//                            true
//                        } catch (e: Exception) {
//                            true
//                        }
//                    }
//                }
            })
    }
}