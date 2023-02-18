@file:OptIn(ExperimentalLayoutApi::class)

package com.cmc12th.runway.ui.detail.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.components.TopGradient
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DetailScreen(appState: ApplicationState, idx: Int) {

    val scrollState = rememberLazyListState()
    ManageSystemBarColor(scrollState)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            state = scrollState
        ) {
            item {
                ShowRoomBanner()
                ShowRoomTitle()
                WidthSpacerLine(height = 1.dp, color = Gray200)
                ShowRoomDetail()

                WidthSpacerLine(height = 1.dp, color = Gray200)
                ShopNews()
                WidthSpacerLine(height = 2.dp, color = Black)
                UserReview()
                HeightSpacer(height = 20.dp)
                WidthSpacerLine(height = 8.dp, color = Gray100)
                BlogReview()
            }
            items((0..3).toList()) {
                BlogReviewItem()
            }
        }

        val topbarColor = remember {
            mutableStateOf(Color.Transparent)
        }
        val topbarIconColor = remember {
            mutableStateOf(Color.White)
        }
        if (scrollState.firstVisibleItemScrollOffset >= 100) {
            topbarColor.value = Color.White
            topbarIconColor.value = Color.Black
        } else {
            topbarColor.value = Color.Transparent
            topbarIconColor.value = Color.White
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(54.dp)
            .background(topbarColor.value)
            .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}, modifier = Modifier
                .padding(start = 20.dp)
                .size(24.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_left_runway),
                    contentDescription = "IC_LEFT_RUNWAY",
                    tint = topbarIconColor.value)
            }
            Row(modifier = Modifier.padding(end = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_border_bookamrk_24),
                        contentDescription = "IC_SHARE",
                        tint = topbarIconColor.value)
                }
                IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_border_share_24),
                        contentDescription = "IC_SHARE",
                        tint = topbarIconColor.value)
                }
            }
        }
    }

}

@Composable
fun ShowRoomTitle() {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text(text = "미니멀 무신사 스튜디오 클럽", style = HeadLine2, color = Color.Black)
        FlowRow {
            RunwayCategory.generateCategoryTags().forEach {
                ShowRoomTag(it)
            }
        }

    }
}

@Composable
fun ShowRoomTag(categoryTag: CategoryTag) {
    Box(modifier = Modifier.padding(top = 4.dp, end = 6.dp)) {
        Row(modifier = Modifier
            .padding(8.dp, 5.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Gray50),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(painter = painterResource(id = categoryTag.iconId),
                contentDescription = "IC_CATEGORY",
                modifier = Modifier.size(12.dp),
                tint = Primary)
            Text(text = categoryTag.name, style = Button2, color = Primary)
        }
    }
}


@Composable
private fun ManageSystemBarColor(scrollState: LazyListState) {

    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        systemUiController.setNavigationBarColor(color = Color.White)
        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.White
            )
        }
    }
    LaunchedEffect(key1 = scrollState.firstVisibleItemScrollOffset) {
        if (scrollState.firstVisibleItemScrollOffset < 100) systemUiController.setSystemBarsColor(
            Color.Transparent)
        else systemUiController.setSystemBarsColor(Color.White)


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
fun BlogReviewItem() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(108.dp)
                .padding(20.dp, 14.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)) {
                Text(text = "무신사 쇼핑 나들이 (무신사 스튜디오 성수/ 무신사 스탠다드 홍대) 졸리고 힘들다.",
                    style = Body1M,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Text(text = "일이 삼사오육 칠 팔구십일이 삼사 오육칠팔 구십 일이삼, 일이 삼사오육 칠 팔구십일이 삼사오육칠팔 구십 일이삼.",
                    style = Body2,
                    color = Gray700,
                    overflow = TextOverflow.Ellipsis)
            }
            WidthSpacer(width = 20.dp)
            Image(
                painter = painterResource(id = R.drawable.img_dummy),
                contentDescription = "IMG_DUMMY",
                modifier = Modifier
                    .size(108.dp),
                contentScale = ContentScale.Crop
            )
        }
        WidthSpacerLine(height = 1.dp, color = Gray200)
    }
}


@Composable
fun UserReview() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "사용자 후기", style = HeadLine4, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                RunwayIconButton(drawable = R.drawable.ic_filled_camera_24, size = 24.dp)
                Text(text = "나도 참여",
                    style = Body1M,
                    color = Primary)
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
private fun ShowRoomDetail() {
    Column(
        modifier = Modifier.padding(20.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_map_18)
            Text(text = "주소가 들어가 예정 강동구, 아해찬-이이이", style = Body2, color = Color.Black)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RunwayIconButton(drawable = R.drawable.ic_border_copy_14, size = 14.dp)
                Text(text = "복사", style = Button2, color = Blue900)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_time_18)
            Text(text = "월 - 일 09:00 ~ 21:00", style = Body2, color = Color.Black)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_call_18)
            Text(text = "0502-1473-3212", style = Body2, color = Color.Black)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_instagram_18)
            Text(text = "[안스타그램 아이디]",
                textDecoration = TextDecoration.Underline,
                style = Body2,
                color = Color.Black)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_web_18)
            Text(text = "[웹사이트 링크]",
                textDecoration = TextDecoration.Underline,
                style = Body2,
                color = Color.Black)
        }
    }

}

@Composable
private fun ShowRoomBanner() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)) {
        Image(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "SHOP_IMAGE",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .aspectRatio(1.2f)
        )
        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 20.dp, alpha = 1f)
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
}