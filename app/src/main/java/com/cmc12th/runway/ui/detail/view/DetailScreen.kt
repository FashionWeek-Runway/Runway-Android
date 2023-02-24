@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)

package com.cmc12th.runway.ui.detail.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.detail.DetailViewModel
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.components.TopGradient
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.PHOTO_REVIEW_ROUTE
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.lang.Float.min

@Composable
fun DetailScreen(
    appState: ApplicationState,
    idx: Int,
    storeName: String,
    onBackPress: () -> Unit = {},
) {

    val detailViewModel: DetailViewModel = hiltViewModel()
    val uiState by detailViewModel.uiState.collectAsState()

    val scrollState = rememberLazyListState()

    val topbarColor = remember {
        mutableStateOf(Color.Transparent)
    }
    val topbarIconColor = remember {
        mutableStateOf(Color.White)
    }
    val topbarIconAnimateColor = animateColorAsState(targetValue = topbarIconColor.value)

    LaunchedEffect(key1 = Unit) {
        detailViewModel.getDetailInfo(idx)
        detailViewModel.getBlogReview(idx, storeName)
    }

    LaunchedEffect(key1 = scrollState.firstVisibleItemScrollOffset) {
        if (scrollState.firstVisibleItemScrollOffset < 100) {
            if (scrollState.firstVisibleItemScrollOffset < 50) {
                topbarColor.value = Color.Transparent
            } else {
                topbarColor.value = Color.White.copy(
                    alpha = min(
                        (scrollState.firstVisibleItemScrollOffset.toFloat()) / 100f,
                        100f
                    )
                )
            }
            topbarIconColor.value = Color.White
        } else {
            topbarColor.value = Color.White
            topbarIconColor.value = Color.Black
        }
    }

    BackHandler {
        onBackPress()
    }

    LaunchedEffect(Unit) {
        appState.bottomBarState.value = false
    }

    ManageSystemBarColor(scrollState, topbarColor.value)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            state = scrollState
        ) {
            item {
                ShowRoomBanner(uiState.storeDetail)
                ShowRoomTitle(uiState.storeDetail)
                ShowRoomDetail(uiState.storeDetail)
            }
            item {
                // WidthSpacerLine(height = 1.dp, color = Gray200)
                // ShopNews()
                WidthSpacerLine(height = 2.dp, color = Black)
                UserReview {
                    appState.navigate(PHOTO_REVIEW_ROUTE)
                }
                HeightSpacer(height = 20.dp)
                WidthSpacerLine(height = 8.dp, color = Gray100)
                BlogReview()
            }
            items(uiState.blogReview) {
                BlogReviewItem(it)
            }

        }

        DetailTopBar(
            topbarColor = topbarColor.value,
            topbarIconAnimateColor = topbarIconAnimateColor.value,
            onBackPress = onBackPress
        )
    }

}

@Composable
private fun BoxScope.DetailTopBar(
    topbarColor: Color,
    topbarIconAnimateColor: Color,
    onBackPress: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(54.dp)
            .background(topbarColor)
            .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackPress() }, modifier = Modifier
                .padding(start = 20.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_runway),
                contentDescription = "IC_LEFT_RUNWAY",
                tint = topbarIconAnimateColor
            )
        }
        Row(
            modifier = Modifier.padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_border_bookamrk_24),
                    contentDescription = "IC_SHARE",
                    tint = topbarIconAnimateColor
                )
            }
            IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_border_share_24),
                    contentDescription = "IC_SHARE",
                    tint = topbarIconAnimateColor
                )
            }
        }
    }
}

@Composable
fun ShowRoomTitle(storeDetail: StoreDetail) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text(text = storeDetail.storeName, style = HeadLine2, color = Color.Black)
        FlowRow {
            storeDetail.category.forEach {
                ShowRoomTag(it)
            }
        }
    }
}

@Composable
fun ShowRoomTag(categoryTag: String) {
    Box(modifier = Modifier.padding(top = 4.dp, end = 6.dp)) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Blue200), RoundedCornerShape(4.dp))
                .background(Color(0x50E6EBFF)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "# $categoryTag", style = Button2, color = Blue600,
                modifier = Modifier
                    .padding(8.dp, 6.dp)
            )
        }
    }
}


@Composable
private fun ManageSystemBarColor(scrollState: LazyListState, topbarColor: Color) {

    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(color = Color.Transparent)
        systemUiController.setNavigationBarColor(color = Color.White)
        onDispose {
            systemUiController.setSystemBarsColor(color = Color.White)
        }
    }
    LaunchedEffect(key1 = scrollState.firstVisibleItemScrollOffset) {
        if (scrollState.firstVisibleItemScrollOffset < 100) {
            systemUiController.setSystemBarsColor(topbarColor)
            systemUiController.setNavigationBarColor(color = Color.White)
        } else systemUiController.setSystemBarsColor(Color.White)
    }
}

@Composable
fun ShopNews() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "소식", modifier = Modifier.weight(1f), style = HeadLine4, color = Black)
            Text(text = "더보기", style = Body2M, color = Gray500)
            IconButton(
                modifier = Modifier.size(18.dp),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    modifier = Modifier.rotate(180f),
                    tint = Gray500
                )
            }
        }
        val shopNews = remember {
            mutableStateOf(listOf("사장님글1", "사장님글2", "사장님글3", "사장님글4"))
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { WidthSpacer(width = 8.dp) }
            items(shopNews.value) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 200.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(4.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "SHOP_NEW_MAIN",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.6f)
                    )
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(max = 80.dp)
                    ) {
                        Text(
                            text = "사장님 글 사장님 글 타이틀사장님 글 타이틀사장님 글 타이글 타이틀사장님 글 타이틀",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = "MM.DD", style = Caption, color = Gray300)
                    }
                }
            }
            item { WidthSpacer(width = 20.dp) }
        }
    }
}

@Composable
fun BlogReview() {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "블로그 후기", style = HeadLine4, color = Color.Black)
    }
    WidthSpacerLine(height = 1.dp, color = Gray200)
}


@Composable
fun BlogReviewItem(blogReview: BlogReview) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .padding(20.dp, 14.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = blogReview.title,
                    style = Body1M,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = blogReview.content,
                    style = Body2,
                    color = Gray700,
                    overflow = TextOverflow.Ellipsis
                )
            }
            WidthSpacer(width = 20.dp)
            AsyncImage(
                modifier = Modifier
                    .size(108.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(blogReview.imgUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "BLOG_IMG",
                contentScale = ContentScale.Crop,
            )
        }
        WidthSpacerLine(height = 1.dp, color = Gray200)
    }
}


@Composable
fun UserReview(navigateToWriteScreen: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "사용자 후기", style = HeadLine4, color = Color.Black)
            Row(
                modifier = Modifier.clickable {
                    navigateToWriteScreen()
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RunwayIconButton(drawable = R.drawable.ic_filled_camera_24, size = 24.dp)
                Text(
                    text = "후기 작성",
                    style = Body1M,
                    color = Primary
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item { WidthSpacer(width = 15.dp) }
            items((0..9).toList()) {
                Image(
                    painter = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_USER_REVIEW",
                    modifier = Modifier.size(132.dp, 200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item { WidthSpacer(width = 15.dp) }
        }
    }
}


@Composable
private fun ShowRoomDetail(storeDetail: StoreDetail) {
    Column(
        modifier = Modifier.padding(20.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_map_18)
            Text(text = storeDetail.address, style = Body2, color = Color.Black)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RunwayIconButton(drawable = R.drawable.ic_border_copy_14, size = 14.dp)
                Text(text = "복사", style = Button2, color = Blue900)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_time_18)
            Text(text = storeDetail.storeTime, style = Body2, color = Color.Black)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_call_18)
            Text(text = storeDetail.storePhone, style = Body2, color = Color.Black)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_instagram_18)
            Text(
                text = storeDetail.instagram,
                textDecoration = TextDecoration.Underline,
                style = Body2,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_web_18)
            Text(
                text = storeDetail.webSite,
                textDecoration = TextDecoration.Underline,
                style = Body2,
                color = Color.Black
            )
        }
    }

}

@Composable
private fun ShowRoomBanner(storeDetail: StoreDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        HorizontalPager(
            pageCount = storeDetail.imgUrlList.size,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .aspectRatio(1.2f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(storeDetail.imgUrlList[it])
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "SHOP_IMAGE",
                contentScale = ContentScale.Crop,
            )
        }

        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 20.dp, alpha = 1f)
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
}