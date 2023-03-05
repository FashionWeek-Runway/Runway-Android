@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.detail.view

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.detail.DetailViewModel
import com.cmc12th.runway.ui.detail.components.*
import com.cmc12th.runway.ui.detail.domain.ManageSystemBarColor
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_WRITE_ROUTE
import com.cmc12th.runway.utils.Constants.WEB_VIEW_ROUTE
import com.cmc12th.runway.utils.getImageUri
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
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
    val context = LocalContext.current

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

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                appState.navController.currentBackStackEntry?.arguments?.putParcelable(
                    "uri",
                    uri
                )
                appState.navigate("$REVIEW_WRITE_ROUTE?idx=$idx")
            }
        }
    val camearLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { takenPhoto ->
            if (takenPhoto != null) {
                getImageUri(context = context, bitmap = takenPhoto)?.let {
                    appState.navController.currentBackStackEntry?.arguments?.putParcelable(
                        "uri",
                        it
                    )
                    appState.navigate("$REVIEW_WRITE_ROUTE?idx=$idx")
                }
            }
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
                ShowRoomDetail(uiState.storeDetail)
                // WidthSpacerLine(height = 1.dp, color = Gray200)
                // ShopNews()
                WidthSpacerLine(height = 2.dp, color = Black)
                UserReview(
                    userReviews = detailViewModel.userReviews,
                    showBottomSheet = showBottomSheet,
                    galleryLauncher = galleryLauncher,
                    cameraLauncher = camearLauncher,
                    navigateToUserReviewDetail = {
                        appState.navigate("$REVIEW_DETAIL_ROUTE?reviewId=${it.reviewId}&viewerType=${ReviewViwerType.STORE_DETAIL.typeToString}")
                    }
                )
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