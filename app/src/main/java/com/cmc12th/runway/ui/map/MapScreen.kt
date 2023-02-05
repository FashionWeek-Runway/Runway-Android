@file:OptIn(ExperimentalNaverMapApi::class)

package com.cmc12th.runway.ui.map

import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.cmc12th.domain.model.NaverItem
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray600
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import ted.gun0912.clustering.naver.TedNaverClustering


@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen() {

    val items = listOf<NaverItem>(
        NaverItem(37.532600, 127.024612),
        NaverItem(37.390791, 127.096306),
        NaverItem(37.540791, 127.096306),
        NaverItem(37.550791, 127.076306),
        NaverItem(37.560791, 127.066306),
        NaverItem(37.550791, 127.166306),
        NaverItem(37.540791, 127.176306),
        NaverItem(37.520791, 127.166306),
        NaverItem(37.510791, 127.016306),
        NaverItem(37.570791, 127.046306),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        /** 네이버 지도 */
        RunwayNaverMap(items)
        /** 검색 및 필터 */
        NaverMapSearch()
    }
}

@Composable
private fun BoxScope.NaverMapSearch() {
    val textFieldValue = remember {
        mutableStateOf(TextFieldValue(""))
    }
    Column(
        modifier = Modifier.align(Alignment.TopCenter)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(20.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(5f)
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                shadowElevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            ) {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = textFieldValue.value,
                    onValueChange = {
                        if (it.selection.length <= 25) textFieldValue.value = it
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
//        keyboardOptions = keyboardOptions ?: KeyboardOptions(),
//        keyboardActions = keyboardActions ?: KeyboardActions(),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(15.dp, 15.dp)
                        ) {
                            Box(Modifier.weight(1f)) {
                                if (textFieldValue.value.text.isEmpty()) {
                                    Text(
                                        "매장명",
                                        style = LocalTextStyle.current.copy(
                                            color = Color.Black.copy(alpha = 0.3f),
                                            fontSize = 16.sp,
                                        ),
                                    )
                                }
                                innerTextField()
                            }
                        }
                    },
                )
            }

            WidthSpacer(width = 10.dp)
            Button(
                modifier = Modifier.fillMaxHeight(),
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Gray600)
            ) {
                Text(text = "검색", color = Color.White)
            }
        }
    }
}


/** NaverMap Compose
 *  https://github.com/fornewid/naver-map-compose */
@Composable
@OptIn(ExperimentalNaverMapApi::class)
private fun RunwayNaverMap(items: List<NaverItem>) {
    NaverMap(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        var clusterManager by remember { mutableStateOf<TedNaverClustering<NaverItem>?>(null) }
        DisposableMapEffect(items) { map ->
            if (clusterManager == null) {
                clusterManager = TedNaverClustering.with<NaverItem>(context, map)
                    .customCluster {
                        TextView(context).apply {
                            text = "${it.size}개"
                            background = AppCompatResources.getDrawable(
                                context,
                                R.drawable.circle_clustor
                            )
                            setPadding(150, 150, 150, 150)
                        }
                    }.customMarker {
                        Marker().apply {
                            icon = OverlayImage.fromResource(R.drawable.ic_nav_mypage_on)
                            width = 60
                            height = 86
                        }
                    }.make()
            }
            clusterManager?.addItems(items)
            onDispose {
                clusterManager?.clearItems()
            }
        }

    }
}

