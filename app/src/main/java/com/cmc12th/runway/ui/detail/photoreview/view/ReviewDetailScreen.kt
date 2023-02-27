@file:OptIn(ExperimentalMaterialApi::class)

package com.cmc12th.runway.ui.detail.photoreview.view

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.CustomBottomSheet
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.detail.photoreview.ReviewViewModel
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.REVIEW_REPORT_ROUTE
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReviewDetailScreen(appState: ApplicationState, reviewId: Int) {

    val bottomsheetState = rememberBottomSheet()
    val coroutineScope = rememberCoroutineScope()

    val reviewViewModel: ReviewViewModel = hiltViewModel()

    val showBottomSheet: (BottomSheetContent) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    }
    LaunchedEffect(key1 = Unit) {
        appState.bottomBarState.value = false
        bottomsheetState.modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
        // TODO 리뷰 디테일 가져오기
    }
    appState.systmeUiController.setStatusBarColor(Color.Black)
    appState.systmeUiController.setNavigationBarColor(Color.Black)

    CustomBottomSheet(
        bottomsheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(Black)
        ) {

            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.img_dummy)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.img_dummy),
                    error = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_REVIEW",
                    contentScale = ContentScale.Crop,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .border(BorderStroke(1.dp, Color.White), CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.img_dummy)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.img_dummy),
                        error = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "IMG_PROFILE",
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = "닉네임 뷰", style = Body2B, color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    RunwayIconButton(drawable = R.drawable.ic_baseline_etc_24, tint = Color.White) {
                        showBottomSheet(BottomSheetContent("",
                            listOf(BottomSheetContentItem("신고",
                                onItemClick = {
                                    appState.navigate("$REVIEW_REPORT_ROUTE?reviewId=$reviewId")
                                }))
                        ))
                    }
                    RunwayIconButton(
                        drawable = R.drawable.ic_close_baseline_small,
                        tint = Color.White) {
                        appState.popBackStack()
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RunwayIconButton(
                        drawable = R.drawable.ic_border_bookmark_24,
                        tint = Color.White,
                        size = 28.dp
                    ) {
                        // TODO Add or Remove Bookmark
                    }
                    Text(text = "7", color = White, style = Caption)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 13.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x50242528))
                    .border(BorderStroke(1.dp, Gray800), RoundedCornerShape(4.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RunwayIconButton(drawable = R.drawable.ic_filled_review_location_16)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "매장명 매장명", style = Body2, color = White)
                        Text(text = "지역명, 성수동", style = Caption, color = Gray300)
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = "IC_DUMMY",
                            tint = Color.White,
                            modifier = Modifier.rotate(180f)
                        )
                    }
                }
            }

        }
    }

}
