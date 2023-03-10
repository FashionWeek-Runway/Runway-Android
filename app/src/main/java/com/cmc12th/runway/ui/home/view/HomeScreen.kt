package com.cmc12th.runway.ui.home.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.home.HomeViewModel
import com.cmc12th.runway.ui.home.component.HomeBannerComponents
import com.cmc12th.runway.ui.home.component.HomeBannerTopBar
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.EDIT_CATEGORY_ROUTE
import com.cmc12th.runway.utils.Constants.HOME_ALL_STORE_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.cmc12th.runway.utils.viewLogEvent

@Composable
fun HomeScreen(appState: ApplicationState, viewModel: HomeViewModel) {

    appState.systmeUiController.setStatusBarColor(Color.Transparent)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        viewLogEvent("HomeScreen")
        viewModel.getHomeBanner(0)
        viewModel.getProfile()
        viewModel.getHomeReview()
    }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = BOTTOM_NAVIGATION_HEIGHT)
            .background(White)
            .fillMaxSize(1f)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black10)
                .aspectRatio(0.67f)
        ) {
            HomeBannerComponents(
                homeBanners = uiState.homeBanners,
                updateBookmark = { storeId, bookmarked ->
                    viewModel.updateBookmark(storeId) {
                        viewModel.updateBookmarkState(storeId, bookmarked)
                    }
                },
                navigateToDetail = { storeId, storeName ->
                    appState.navigate(
                        "${DETAIL_ROUTE}?storeId=$storeId&storeName=$storeName"
                    )
                },
                navigateToShowMoreStore = {
                    appState.navigate(HOME_ALL_STORE_ROUTE)
                }
            )

            /** Banner Top */
            HomeBannerTopBar(
                nickname = uiState.nickName,
                navigateToEditCategory = {
                    appState.navigate("${EDIT_CATEGORY_ROUTE}?nickName={${uiState.nickName}")
                },
                navigateToShowMoreStore = {
                    appState.navigate(HOME_ALL_STORE_ROUTE)
                }
            )
        }

        HomeReviews(
            reviews = reviews,
            navigateToUserReviewDetail = { index ->
                appState.navigate("${REVIEW_DETAIL_ROUTE}?reviewId=${index}&viewerType=${ReviewViwerType.HOME.typeToString}")
            },
        )
        ShowNews()
    }
}

@Composable
private fun ShowNews() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 30.dp),
        text = "???????????? ?????? ????????? ???????????????",
        style = HeadLine4,
        color = Color.Black
    )

    EmptyResultView(
        drawableid = R.mipmap.img_empty_notice,
        title = "????????? ?????? ????????????",
    )

//    Column(
//        modifier = Modifier
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp)
//    ) {
//        (0..4).toList().forEach {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(14.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.img_dummy),
//                    contentDescription = "IMG_STORE_NEWS",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1.39f),
//                    contentScale = ContentScale.Crop
//                )
//                Text(text = "'PRESENT' 'FUTURE' 'PAST'", style = Body1B, color = Gray900)
//            }
//        }
//    }
}

@Composable
private fun HomeReviews(
    reviews: LazyPagingItems<HomeReviewItem>,
    navigateToUserReviewDetail: (Int) -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        text = "????????? ????????? ????????? ??????",
        style = HeadLine4,
        color = Color.Black
    )

    HeightSpacer(height = 16.dp)

    when (reviews.loadState.refresh) {
        LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(114.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center),
                    color = Primary,
                    strokeWidth = 4.dp,
                )
            }
        }
        is LoadState.Error -> {
            EmptyResultView(
                drawableid = R.mipmap.img_empty_home_review,
                title = "???????????? ????????? ??????????????????.",
            )
        }
        is LoadState.NotLoading -> {
            if (reviews.itemCount == 0) {
                EmptyResultView(
                    drawableid = R.mipmap.img_empty_home_review,
                    title = "?????? ??? ????????? ????????? ?????????.",
                    subtitle = "????????? ??????????????? ????????????\n????????? ????????? ???????????????."
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item { WidthSpacer(width = 15.dp) }

                items(reviews.itemCount) { index ->
                    Box(modifier = Modifier
                        .size(132.dp, 200.dp)
                        .clickable {
                            navigateToUserReviewDetail(reviews[index]?.reviewId ?: 0)
                        }) {
                        AsyncImage(
                            modifier = Modifier
                                .background(Gray100)
                                .fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(reviews[index]?.imgUrl)
                                .crossfade(true)
                                .build(),
                            error = painterResource(id = R.drawable.img_dummy),
                            contentDescription = "IMG_PROFILE",
                            contentScale = ContentScale.Crop,
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filled_review_location_16),
                                contentDescription = "IC_LOCATION",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = reviews[index]?.regionInfo ?: "",
                                style = Caption,
                                color = Gray100
                            )
                        }

                        if (reviews[index]?.read == true) {
                            Box(
                                modifier = Modifier
                                    .background(Black40)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
                item { WidthSpacer(width = 15.dp) }
            }
        }
    }


}

@Composable
private fun EmptyResultView(
    @DrawableRes drawableid: Int,
    title: String,
    subtitle: String = "",
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = drawableid),
            contentDescription = "IMG_EMPTY_HOME_REVIEW",
            modifier = Modifier.size(128.dp, 115.dp)
        )
        HeightSpacer(height = 30.dp)
        Text(text = title, style = Body1, color = Color.Black, textAlign = TextAlign.Center)
        HeightSpacer(height = 5.dp)
        Text(text = subtitle, style = Body2, color = Gray500, textAlign = TextAlign.Center)
    }
}

