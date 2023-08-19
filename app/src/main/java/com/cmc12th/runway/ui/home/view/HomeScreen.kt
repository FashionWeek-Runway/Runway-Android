@file:OptIn(
    ExperimentalGlideComposeApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class
)

package com.cmc12th.runway.ui.home.view

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.domain.model.response.home.HomeInstaResponse
import com.cmc12th.runway.R
import com.cmc12th.domain.model.response.home.HomeReviewItem
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager


@Composable
fun HomeScreen(appState: ApplicationState, viewModel: HomeViewModel) {

    appState.systmeUiController.setStatusBarColor(Color.Transparent)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    val instas = viewModel.instas.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewLogEvent("HomeScreen")
        viewModel.getHomeBanner(0)
        viewModel.getProfile()
        viewModel.getHomeReview()
        viewModel.getInsta()
    }

    LazyColumn(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = BOTTOM_NAVIGATION_HEIGHT)
            .background(White)
            .fillMaxSize(1f)
    ) {

        item {
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

                /** Banner Top */
                HomeBannerTopBar(
                    nickname = uiState.nickName,
                    navigateToEditCategory = {
                        appState.navigate("${EDIT_CATEGORY_ROUTE}?nickName=${uiState.nickName}")
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 30.dp),
                text = "흥미로운 가게 소식을 알려드려요",
                style = HeadLine4,
                color = Color.Black
            )
        }

        item {
            HeightSpacer(height = 8.dp)
        }
        items(instas.itemCount) { idx ->
            Column(
                modifier = Modifier
                    .padding(20.dp, 8.dp)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Gray200),
                    count = instas[idx]?.imgList?.size ?: 0,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .background(Gray200)
                            .fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(instas[idx]?.imgList?.get(it))
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.ic_defailt_profile),
                        contentDescription = "IMG_SELECTED_IMG",
                        contentScale = ContentScale.Crop,
                    )
                }

                HeightSpacer(height = 10.dp)
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(instas[idx]?.instaLink)
                            )
                        )
                    }) {
                    Text(
                        text = instas[idx]?.storeName ?: "",
                        style = Body1B,
                        color = Gray900
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_home_insta_link_16),
                            contentDescription = "IC_INSTAGRAM",
                            modifier = Modifier.size(16.dp),
                        )
                        Text(text = "인스타그램", style = Body2M, color = Gray500)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeReviews(
    reviews: LazyPagingItems<HomeReviewItem>,
    navigateToUserReviewDetail: (Int) -> Unit,
) {

    val density = LocalDensity.current

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        text = "비슷한 취향의 사용자 후기",
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
                title = "네트워크 연결을 확인해주세요.",
            )
        }

        is LoadState.NotLoading -> {
            if (reviews.itemCount == 0) {
                EmptyResultView(
                    drawableid = R.mipmap.img_empty_home_review,
                    title = "아직 내 취향의 후기가 없어요.",
                    subtitle = "스타일 카테고리를 추가해서\n다양한 후기를 만나보세요."
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
                        GlideImage(
                            modifier = Modifier
                                .background(Gray100)
                                .fillMaxSize(),
                            model = reviews[index]?.imgUrl,
                            contentDescription = "IMG_REVIEW",
                            contentScale = ContentScale.Crop
                        ) {
                            it
                                .priority(Priority.IMMEDIATE)
                                .override(
                                    (132 * density.density).toInt(),
                                    (200 * density.density).toInt()
                                )
                                .signature(ObjectKey(reviews[index]?.imgUrl ?: -1))
                        }

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

