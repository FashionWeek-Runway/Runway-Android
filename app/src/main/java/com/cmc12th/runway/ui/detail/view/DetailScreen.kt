package com.cmc12th.runway.ui.detail.view

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.TopGradient
import com.cmc12th.runway.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DetailScreen(appState: ApplicationState, idx: Int) {

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        item {
            ShowRoomBanner()
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
    Column {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "블로그 후기", style = Body1M, color = Color.Black)
        }
        WidthSpacerLine(height = 1.dp, color = Gray200)
    }
}


@Composable
fun BlogReviewItem() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 14.dp),
        ) {
            Text(
                text = "무신사 쇼핑 나들이 (무신사 스튜디오 성수/ 무신사 스탠다드 홍대) 졸리고 힘들다.",
                modifier = Modifier.weight(1f),
                style = Body2M
            )
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
            Text(text = "사용자 후기", style = Body1M, color = Color.Black)
            Text(text = "나도 참여", style = Body2, textDecoration = TextDecoration.Underline)
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
    Box(modifier = Modifier.padding(20.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
        }
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text(text = "북마크")
        }
    }
}

@Composable
private fun ShowRoomBanner() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close_fill_circle),
            contentDescription = "IC_CLOSE_FILL_CIRCLE",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
                .size(32.dp),
            tint = Color.Unspecified
        )
        Image(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "SHOP_IMAGE",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TODO 글꼴 미적용
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White)
            ) {
                Text(
                    text = "미니멀", modifier = Modifier
                        .padding(10.dp, 6.dp),
                    style = Body2
                )
            }
            Text(text = "무신사 스텐다드", color = Color.White, fontSize = 26.sp)
        }
        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 30.dp, alpha = 1f)
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
}