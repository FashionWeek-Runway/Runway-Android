@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalPagerApi::class, ExperimentalTextApi::class,
    ExperimentalPagerApi::class, ExperimentalPagerApi::class
)

package com.cmc12th.runway.ui.home.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.home.HomeBannerItem
import com.cmc12th.runway.data.response.home.HomeReviewItem
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.home.HomeViewModel
import com.cmc12th.runway.ui.home.component.HomeBannerStep
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Job

@Composable
fun HomeScreen(appState: ApplicationState) {

    appState.systmeUiController.setStatusBarColor(Color.Transparent)
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
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
            HomeBanner(
                homeBanners = uiState.homeBanners
            ) { storeId, bookmarked ->
                viewModel.updateBookmark(storeId) {
                    viewModel.updateBookmarkState(storeId, bookmarked)
                }
            }

            /** Banner Top */
            MainBannerTopBar(uiState.nickName)
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

        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            (0..4).toList().forEach {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "IMG_STORE_NEWS",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.39f),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "'PRESENT' 'FUTURE' 'PAST'", style = Body1B, color = Gray900)
                }
            }
        }

    }
}

@Composable
private fun HomeReviews(
    reviews: LazyPagingItems<HomeReviewItem>,
    navigateToUserReviewDetail: (Int) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        text = "비슷한 취향의 사용자 후기",
        style = HeadLine4,
        color = Color.Black
    )

    HeightSpacer(height = 16.dp)

    if (reviews.itemCount == 0) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.mipmap.img_empty_home_review),
                contentDescription = "IMG_EMPTY_HOME_REVIEW",
                modifier = Modifier.size(128.dp, 115.dp)
            )
            HeightSpacer(height = 30.dp)
            Text(text = "아직 내 취향의 후기가 없어요.", style = Body1, color = Color.Black)
            HeightSpacer(height = 5.dp)
            Text(text = "스타일 카테고리를 추가해서\n다양한 후기를 만나보세요.", style = Body2, color = Gray500)
        }
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
                        .fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(reviews[index]?.imgUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.img_dummy),
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
            }
        }
        item { WidthSpacer(width = 15.dp) }
    }
}

@Composable
private fun BoxScope.HomeBanner(
    homeBanners: MutableList<HomeBannerItem>,
    updateBookmark: (storeId: Int, bookmarked: Boolean) -> Unit
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize(),
        count = homeBanners.size,
    ) { page ->
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray200),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(homeBanners[page].imgUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "SHOP_IMAGE",
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .padding(top = 130.dp, start = 39.dp, end = 39.dp, bottom = 20.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .border(BorderStroke(1.dp, White), RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .align(Alignment.TopCenter)
                        .size(12.dp)
                        .border(BorderStroke(1.dp, White), CircleShape)
                )

                Box(
                    modifier = Modifier
                        .padding(11.dp)
                        .align(Alignment.TopEnd)
                ) {
                    if (homeBanners[page].bookmark) {
                        IconButton(modifier = Modifier
                            .size(26.dp),
                            onClick = {
                                updateBookmark(homeBanners[page].storeId, false)
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filled_bookmark_24),
                                contentDescription = "IC_BORDER_BOOKMARK",
                                tint = White
                            )
                        }
                    } else {
                        IconButton(modifier = Modifier
                            .size(26.dp),
                            onClick = {
                                updateBookmark(homeBanners[page].storeId, true)
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_border_bookmark_24),
                                contentDescription = "IC_BORDER_BOOKMARK",
                                tint = White
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Text(
                            text = homeBanners[page].storeName,
                            style = TextStyle.Default.copy(
                                fontSize = 26.sp,
                                fontFamily = blackHanSans,
                                drawStyle = Stroke(
                                    miter = 10f,
                                    width = 8f,
                                    join = StrokeJoin.Round,
                                ),
                            ),
                            color = Color.White
                        )
                        Text(
                            text = homeBanners[page].storeName,
                            style = TextStyle.Default.copy(
                                fontSize = 26.sp,
                                fontFamily = blackHanSans
                            ),
                            color = Primary
                        )
                    }
                    HeightSpacer(height = 14.dp)
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .background(Point)
                            .width(30.dp)
                    )
                    HeightSpacer(height = 14.dp)
                    Row(
                        modifier = Modifier
                            .background(Primary)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location_empty_small_14),
                            contentDescription = "IC_LOCATION_EMPTY_SAMLL",
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Text(
                            text = homeBanners[page].regionInfo,
                            color = Color.White,
                            style = Body2M,
                        )
                    }

                    Text(
                        text = "#페미닌 #미니멀 #시티보이",
                        color = Point,
                        style = Button2,
                        modifier = Modifier
                            .background(Primary)
                            .padding(4.dp)
                    )
                    HeightSpacer(height = 16.dp)
                    Image(
                        painter = painterResource(id = R.mipmap.img_runway_barcord),
                        contentDescription = "IMG_RUNWAY_BARCORD",
                        modifier = Modifier.size(63.dp, 21.dp)
                    )
                }

            }


        }
    }
    HomeBannerStep(
        modifier = Modifier.align(Alignment.BottomCenter),
        step = pagerState.currentPage + 1,
        maxSize = homeBanners.size
    )
}

@Composable
private fun BoxScope.MainBannerTopBar(nickname: String) {
    Box(
        Modifier.Companion
            .statusBarsPadding()
            .align(Alignment.TopCenter)
            .padding(20.dp, top = 10.dp, 20.dp, 20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            RunwayIconButton(
                drawable = R.drawable.ic_baseline_filter_24,
                tint = Color.White
            ) {
                // TODO 필터 설정 뷰로 넘기기
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "${nickname}님의\n취향을 가득 담은 매장", style = HeadLine4, color = White)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "전체보기", style = Body2M, color = White)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    modifier = Modifier
                        .rotate(180f)
                        .size(12.dp),
                    tint = Color.White
                )
            }
        }
    }
}