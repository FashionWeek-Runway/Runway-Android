@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class, ExperimentalPagerApi::class)

package com.cmc12th.runway.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

@Composable
fun HomeScreen() {

    Column(modifier = Modifier
        .statusBarsPadding()
        .background(White)
        .fillMaxSize(1f)) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            horizontalArrangement = Arrangement.End) {
            RunwayIconButton(drawable = R.drawable.ic_baseline_filter_24, tint = Color.Black) {
                // TODO 필터 설정 뷰로 넘기기
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, top = 0.dp, 20.dp, 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom) {
            Text(text = "나패피님의\n취향을 가득 담은 매장", style = HeadLine4, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "전체보기", style = Body2M, color = Gray600)
                Icon(painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    modifier = Modifier
                        .rotate(180f)
                        .size(12.dp))
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f),
            count = 6,
            contentPadding = PaddingValues(start = 27.dp, end = 27.dp),
        ) { page ->
            Box(
                Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.92f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        alpha = lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxSize()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.img_dummy)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.img_dummy),
                        error = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "SHOP_IMAGE",
                        contentScale = ContentScale.Crop,
                    )
                    Column(modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "제목이 들어갑니다.", color = White, style = HeadLine2)
                        HeightSpacer(height = 2.dp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_filled_review_location_16),
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified,
                                contentDescription = "IC_LOCATION")
                            Text(text = "성수동, 서울", style = Body2, color = Gray50)
                        }
                        HeightSpacer(height = 15.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (0..4).toList().forEach {
                                Text(text = "# 미니멀", style = Button2, color = Gray50)
                            }
                        }

                    }

                }
            }
        }

        HeightSpacer(height = 40.dp)


        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            text = "비슷한 취향의 사용자 후기",
            style = HeadLine4,
            color = Color.Black)
        HeightSpacer(height = 16.dp)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item { WidthSpacer(width = 15.dp) }

            items((0..8).toList()) {
                AsyncImage(
                    modifier = Modifier
                        .size(132.dp, 200.dp)
                        .clickable {
                        },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.img_dummy),
                    error = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_REVIEW",
                    contentScale = ContentScale.Crop,
                )
            }
            item { WidthSpacer(width = 15.dp) }
        }
    }
}