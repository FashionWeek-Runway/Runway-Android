@file:OptIn(
    ExperimentalMaterialApi::class,
)

package com.cmc12th.runway.ui.detail.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.ui.components.CustomBottomSheet
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.detail.DetailViewModel
import com.cmc12th.runway.ui.detail.components.*
import com.cmc12th.runway.ui.detail.domain.ManageSystemBarColor
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.theme.Gray100
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_WRITE_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE
import com.cmc12th.runway.utils.DETAIL_TOUCH_EVENT
import com.cmc12th.runway.utils.clickLogEvent
import com.cmc12th.runway.utils.lookupLogEvent
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(
    appState: ApplicationState,
    idx: Int,
    storeName: String,
    onBackPress: () -> Unit = {},
) {
    val detailViewModel: DetailViewModel = hiltViewModel()
    val uiState by detailViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    val topbarColor = remember {
        mutableStateOf(Color.Transparent)
    }
    val topbarIconColor = remember {
        mutableStateOf(Color.White)
    }
    val topbarIconAnimateColor = animateColorAsState(targetValue = topbarIconColor.value)

    val bottomsheetState = rememberBottomSheet()
    val showBottomSheet: (@Composable () -> Unit) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.contents.value = it
            bottomsheetState.modalSheetState.show()
        }
    }
    val navigateToReviewWrite: (Uri?) -> Unit = { uri ->
        if (uri != null) {
            appState.navController.currentBackStackEntry?.arguments?.putParcelable(
                "uri",
                uri
            )
            appState.navigate("$REVIEW_WRITE_ROUTE?idx=$idx")
        }
    }
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = hasImage) {
        if (hasImage) {
            navigateToReviewWrite(imageUri)
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            navigateToReviewWrite(uri)
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            hasImage = it
        }

    ManageSystemBarColor(
        scrollState = scrollState,
        topbarColor = topbarColor.value,
        updateTopbarIconColor = { topbarIconColor.value = it },
        updateTopbarColor = { topbarColor.value = it }
    )

    LaunchedEffect(key1 = Unit) {
        appState.bottomBarState.value = false
        detailViewModel.getUserReviewPaging(idx)
        detailViewModel.getDetailInfo(idx)
        detailViewModel.getBlogReview(idx, storeName)
        lookupLogEvent("map_detail")
    }

    BackHandler {
        onBackPress()
    }

    CustomBottomSheet(
        bottomsheetState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .verticalScroll(scrollState),
            ) {
                ShowRoomBanner(uiState.storeDetail)
                ShowRoomTitle(uiState.storeDetail)
                ShowRoomDetail(
                    storeDetail = uiState.storeDetail,
                    navigateToWeb = { storeUrl ->
                        clickLogEvent(DETAIL_TOUCH_EVENT, "mapdetail_area_04")
                        appState.navigate("$WEB_VIEW_ROUTE?title=${""}&url=${storeUrl}")
                    },
                    navigateToInstgram = { instagramUrl ->
                        clickLogEvent(DETAIL_TOUCH_EVENT, "mapdetail_area_03")
                        intentToInstagram(
                            instagramUrl = instagramUrl,
                            context = context
                        )
                    },
                    showSnackbar = { message ->
                        appState.showSnackbar(message)
                    }
                )
                ShowRoomInfoInCorrect(
                    showBottomSheet = showBottomSheet,
                    hideBottomSheet = {
                        coroutineScope.launch {
                            bottomsheetState.modalSheetState.hide()
                        }
                    },
                    reportStore = { reportContents ->
                        detailViewModel.reportStore(
                            storeId = idx,
                            reportContents = reportContents,
                            showSnackBar = {
                                appState.showSnackbar(it)
                            }
                        )
                    }
                )
                WidthSpacerLine(height = 8.dp, color = Gray100)
                UserReview(
                    userReviews = detailViewModel.userReviews,
                    showBottomSheet = showBottomSheet,
                    hideBottomSheet = {
                        coroutineScope.launch {
                            bottomsheetState.modalSheetState.hide()
                        }
                    },
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = cameraLauncher,
                    updateImageUri = { imageUri = it },
                    navigateToUserReviewDetail = {
                        clickLogEvent(DETAIL_TOUCH_EVENT, "mapdetail_area_05")
                        appState.navigate("$REVIEW_DETAIL_ROUTE?reviewId=${it.reviewId}&viewerType=${ReviewViwerType.STORE_DETAIL.typeToString}")
                    }
                )
                HeightSpacer(height = 20.dp)
                WidthSpacerLine(height = 8.dp, color = Gray100)
                BlogReview(
                    blogReview = uiState.blogReview,
                    isMoreBtnVisible = uiState.isMoreBtnVisible,
                    isBlogReviewExapnded = uiState.isBlogReviewExapnded,
                    naviagteToWebView = { url, title ->
                        appState.navigate("${WEB_VIEW_ROUTE}?title$title=&url=$url")
                        clickLogEvent(DETAIL_TOUCH_EVENT, "mapdetail_area_06")
                    },
                    updateExpandedState = {
                        detailViewModel.updateExpandedState()
                    }
                )

            }

            DetailTopBar(
                topbarColor = topbarColor.value,
                topbarIconAnimateColor = topbarIconAnimateColor.value,
                onBackPress = onBackPress,
                isBookmarked = uiState.storeDetail.bookmark,
                updateBookmark = {
                    detailViewModel.updateBookmark(idx) {
                        if (it) {
                            appState.showSnackbar("매장이 저장되었습니다.")
                        } else {
                            appState.showSnackbar("매장이 저장이 취소되었습니다.")
                        }
                        detailViewModel.updateBookmarkState(it)
                    }
                }
            )
        }
    }
}


private fun intentToInstagram(instagramUrl: String, context: Context) {
    val uri = Uri.parse(instagramUrl)
    val instaIntent = Intent(Intent.ACTION_VIEW, uri)
    instaIntent.setPackage("com.instagram.android")
    try {
        context.startActivity(instaIntent)
    } catch (e: ActivityNotFoundException) {
        // 인스타그램이 설치되어 있지 않다면 Web에서 해당페이지를 연다.
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(instagramUrl)
            )
        )
    }
}
