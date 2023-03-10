@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
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
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Gray100
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_WRITE_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE
import com.cmc12th.runway.utils.viewLogEvent
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
    val showBottomSheet: (BottomSheetContent) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
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

    val camearLauncher =
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
        viewLogEvent("DetailScreen")
        appState.bottomBarState.value = false
        detailViewModel.getUserReviewPaging(idx)
        detailViewModel.getDetailInfo(idx)
        detailViewModel.getBlogReview(idx, storeName)
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
                        appState.navigate("$WEB_VIEW_ROUTE?title=${""}&url=${storeUrl}")
                    },
                    navigateToInstgram = { instagramUrl ->
                        intentToInstagram(
                            instagramUrl = instagramUrl,
                            context = context
                        )
                    },
                    showSnackbar = { message ->
                        appState.showSnackbar(message)
                    }
                )
                WidthSpacerLine(height = 2.dp, color = Black)
                UserReview(
                    userReviews = detailViewModel.userReviews,
                    showBottomSheet = showBottomSheet,
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = camearLauncher,
                    updateImageUri = { imageUri = it },
                    navigateToUserReviewDetail = {
                        appState.navigate("$REVIEW_DETAIL_ROUTE?reviewId=${it.reviewId}&viewerType=${ReviewViwerType.STORE_DETAIL.typeToString}")
                    }
                )
                HeightSpacer(height = 20.dp)
                WidthSpacerLine(height = 8.dp, color = Gray100)
                BlogReview()
                uiState.blogReview.map {
                    BlogReviewItem(
                        blogReview = it,
                        onClick = { url, title ->
                            appState.navigate("$WEB_VIEW_ROUTE?title$title=&url=$url")
                        }
                    )
                }
            }

            DetailTopBar(
                topbarColor = topbarColor.value,
                topbarIconAnimateColor = topbarIconAnimateColor.value,
                onBackPress = onBackPress,
                isBookmarked = uiState.storeDetail.bookmark,
                updateBookmark = {
                    detailViewModel.updateBookmark(idx) {
                        if (it) {
                            appState.showSnackbar("????????? ?????????????????????.")
                        } else {
                            appState.showSnackbar("????????? ????????? ?????????????????????.")
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
        // ?????????????????? ???????????? ?????? ????????? Web?????? ?????????????????? ??????.
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(instagramUrl)
            )
        )
    }
}
