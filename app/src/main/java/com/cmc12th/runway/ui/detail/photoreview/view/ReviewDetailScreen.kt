@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.detail.photoreview.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cmc12th.domain.model.response.store.UserReviewDetail
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BottomSheetUsingItemLists
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.detail.photoreview.ReviewViewModel
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Body2B
import com.cmc12th.runway.ui.theme.Caption
import com.cmc12th.runway.ui.theme.Error_Color
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray800
import com.cmc12th.runway.ui.theme.Point
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.ui.theme.White
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.REVIEW_REPORT_ROUTE
import com.cmc12th.runway.utils.lookupLogEvent
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class ReviewMoveDirection {
    LEFT, RIGHT, DEFAULT;
}

@Composable
fun ReviewDetailScreen(
    appState: ApplicationState,
    argReviewId: Int,
    viewerType: ReviewViwerType,
) {

    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val bottomsheetState = rememberBottomSheet()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var reviewId = argReviewId
    val uiState by reviewViewModel.uiState.collectAsStateWithLifecycle()

    val showBottomSheet: (BottomSheetContent) -> Unit = {
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.show()
        }
    }

    LaunchedEffect(key1 = uiState) {
        coroutineScope.launch {
            context.imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(uiState.reviewDetail.reviewInquiry.nextReviewImgUrl)
                    .data(uiState.reviewDetail.reviewInquiry.nextReviewImgUrl)
                    .build()
            )
            context.imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .diskCacheKey(uiState.reviewDetail.reviewInquiry.prevReviewImgUrl)
                    .data(uiState.reviewDetail.reviewInquiry.prevReviewImgUrl)
                    .build()
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        when (viewerType) {
            ReviewViwerType.STORE_DETAIL -> {
                reviewViewModel.getReviewDetailStore(reviewId)
            }

            ReviewViwerType.MYPAGE -> {
                reviewViewModel.getReviewDetailMypage(reviewId)
            }

            ReviewViwerType.HOME -> {
                reviewViewModel.getReviewDetailHome(reviewId = reviewId)
            }

            ReviewViwerType.BOOKMARK -> {
                reviewViewModel.getReviewDetailBookmark(reviewId)
            }
        }
        appState.bottomBarState.value = false
        bottomsheetState.modalSheetState.hide()
        lookupLogEvent("review_01")
    }
    appState.systmeUiController.setStatusBarColor(Color.Black)
    appState.systmeUiController.setNavigationBarColor(Color.Black)

    BottomSheetUsingItemLists(
        bottomsheetState
    ) {
        DetailContents(
            showSnackBar = { appState.showSnackbar(it) },
            reviewDetail = uiState.reviewDetail,
            showBottomSheet = showBottomSheet,
            updateBookmark = {
                reviewViewModel.updateBookmark(reviewId) {
                    appState.showSnackbar(
                        if (uiState.reviewDetail.bookmark) "리뷰가 저장되었습니다." else "리뷰 저장이 취소됬습니다."
                    )
                }
            },
            popBackStack = {
                appState.popBackStack()
            },
            navigateToReportScreen = {
                appState.navigate("$REVIEW_REPORT_ROUTE?reviewId=$reviewId")
            },
            getReviewDetail = { idx, onSuccess ->
                reviewId = idx
                when (viewerType) {
                    ReviewViwerType.STORE_DETAIL -> {
                        reviewViewModel.getReviewDetailStore(idx) {
                            onSuccess()
                        }
                    }

                    ReviewViwerType.MYPAGE -> {
                        reviewViewModel.getReviewDetailMypage(idx) {
                            onSuccess()
                        }
                    }

                    ReviewViwerType.HOME -> {
                        reviewViewModel.getReviewDetailHome(idx) {
                            onSuccess()
                        }
                    }

                    ReviewViwerType.BOOKMARK -> {
                        reviewViewModel.getReviewDetailBookmark(idx) {
                            onSuccess()
                        }
                    }
                }
            },
            deleteReview = {
                reviewViewModel.deleteReview(reviewId = reviewId, onSuccess = {
                    appState.showSnackbar("리뷰가 삭제되었습니다.")
                    appState.popBackStack()
                })
            },
            navigateToDetail = { storeId, storeName ->
                appState.navigate(
                    "${Constants.DETAIL_ROUTE}?storeId=$storeId&storeName=$storeName"
                )
            }
        )
    }

}

@Composable
private fun DetailContents(
    showBottomSheet: (BottomSheetContent) -> Unit,
    reviewDetail: UserReviewDetail,
    navigateToReportScreen: () -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (String) -> Unit,
    updateBookmark: () -> Unit,
    getReviewDetail: (idx: Int, onSuccess: () -> Unit) -> Unit,
    deleteReview: () -> Unit,
    navigateToDetail: (storeId: Int, storeName: String) -> Unit,
) {

    var offsetX by remember { mutableStateOf(0.dp.value) }
    val animateOffsetX = animateFloatAsState(targetValue = offsetX)

    val updateOffestX: (mx: Float) -> Unit = {
        val dx = offsetX + it * 1.4f
        offsetX = dx
    }
    var reviewDirection by remember {
        mutableStateOf(ReviewMoveDirection.DEFAULT)
    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthFloat = with(density) { configuration.screenWidthDp.dp.toPx() }

    val loadPreviousReview = {
        val prevReviewId = reviewDetail.reviewInquiry.prevReviewId
        if (prevReviewId == null) {
            offsetX = 0f
            showSnackBar("이전 리뷰가 존재하지 않습니다.")
        } else {
            offsetX = -screenWidthFloat
            getReviewDetail(prevReviewId) {
                offsetX = 0f
            }
        }
    }

    val loadNextReview = {
        val nextReviewId = reviewDetail.reviewInquiry.nextReviewId
        if (nextReviewId == null) {
            offsetX = 0f
            showSnackBar("다음 리뷰가 존재하지 않습니다.")
        } else {
            offsetX = screenWidthFloat
            getReviewDetail(nextReviewId) {
                offsetX = 0f
            }
        }
    }

    LaunchedEffect(key1 = reviewDirection) {
        when (reviewDirection) {
            ReviewMoveDirection.LEFT -> {
                loadPreviousReview()
                reviewDirection = ReviewMoveDirection.DEFAULT
            }

            ReviewMoveDirection.RIGHT -> {
                loadNextReview()
                reviewDirection = ReviewMoveDirection.DEFAULT
            }

            ReviewMoveDirection.DEFAULT -> {
                // TO NOTHING
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Black),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            MainImage(
                offsetX = animateOffsetX.value,
                reviewDetail = reviewDetail
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (abs(offsetX) <= screenWidthFloat / 2) {
                                offsetX = 0f
                            } else if (offsetX < -(screenWidthFloat / 2)) {
                                reviewDirection = ReviewMoveDirection.RIGHT
                            } else if (offsetX > screenWidthFloat / 2) {
                                reviewDirection = ReviewMoveDirection.LEFT
                            }
                        }
                    ) { change, dragAmount ->
                        updateOffestX(dragAmount.x)
                        change.consume()
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        reviewDirection =
                            if (screenWidthFloat / 2 > it.x) {
                                ReviewMoveDirection.LEFT
                            } else {
                                ReviewMoveDirection.RIGHT
                            }
                    }
                }
            )
            Topbar(
                reviewDetail,
                showBottomSheet,
                navigateToReportScreen,
                popBackStack,
                deleteReview
            )
            Bookmark(reviewDetail, updateBookmark)
        }
        BottomBar(
            reviewDetail = reviewDetail,
            navigateToDetail = navigateToDetail
        )
    }
}

@Composable
private fun MainImage(
    offsetX: Float,
    reviewDetail: UserReviewDetail,
) {

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(reviewDetail.imgUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(reviewDetail.imgUrl)
            .crossfade(true)
            .build(),
        loading = {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center),
                    color = Primary,
                    strokeWidth = 4.dp,
                )
            }
        },
        contentDescription = "IMG_REVIEW",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .background(Black)
            .fillMaxSize()
            .offset {
                IntOffset(x = offsetX.roundToInt(), y = 0)
            },
    )

}


@Composable
private fun BottomBar(
    reviewDetail: UserReviewDetail,
    navigateToDetail: (storeId: Int, storeName: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 13.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0x50242528))
            .border(BorderStroke(1.dp, Gray800), RoundedCornerShape(4.dp))
            .clickable {
                navigateToDetail(reviewDetail.storeId, reviewDetail.storeName)
            }
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
                Text(
                    text = reviewDetail.storeName,
                    style = Body2,
                    color = White,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = reviewDetail.regionInfo,
                    style = Caption,
                    color = Gray300,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    tint = Color.White,
                    modifier = Modifier.rotate(180f)
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Topbar(
    reviewDetail: UserReviewDetail,
    showBottomSheet: (BottomSheetContent) -> Unit,
    navigateToReportScreen: () -> Unit,
    popBackStack: () -> Unit,
    deleteReview: () -> Unit,
) {
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
                .background(Gray200)
                .border(BorderStroke(1.dp, Color.White), CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(reviewDetail.profileImgUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_defailt_profile),
            error = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IMG_PROFILE",
            contentScale = ContentScale.Crop,
        )
        Text(
            text = reviewDetail.nickname, style = Body2B, color = Color.White,
            modifier = Modifier.weight(1f)
        )
        RunwayIconButton(drawable = R.drawable.ic_baseline_etc_24, tint = Color.White) {
            showBottomSheet(
                BottomSheetContent(
                    "",
                    if (reviewDetail.my) {
                        listOf(
                            BottomSheetContentItem(
                                itemName = "삭제",
                                itemTextColor = Error_Color,
                                onItemClick = { deleteReview() }
                            )
                        )
                    } else {
                        listOf(
                            BottomSheetContentItem(
                                "신고",
                                onItemClick = navigateToReportScreen
                            )
                        )
                    }
                )
            )
        }
        RunwayIconButton(
            drawable = R.drawable.ic_close_baseline_small,
            tint = Color.White
        ) {
            popBackStack()
        }
    }
}

@Composable
private fun BoxScope.Bookmark(
    reviewDetail: UserReviewDetail,
    updateBookmark: () -> Unit,
) {
    if (!reviewDetail.my) {
        Column(
            modifier = Modifier.Companion
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (reviewDetail.bookmark) {
                RunwayIconButton(
                    drawable = R.drawable.ic_filled_bookmark_24,
                    tint = Point,
                    size = 28.dp,
                    onCLick = { updateBookmark() }
                )
            } else {
                RunwayIconButton(
                    drawable = R.drawable.ic_border_bookmark_24,
                    tint = Color.White,
                    size = 28.dp,
                    onCLick = { updateBookmark() }
                )
            }
        }
    }
}

