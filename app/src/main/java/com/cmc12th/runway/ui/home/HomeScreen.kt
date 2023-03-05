@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalPagerApi::class, ExperimentalTextApi::class
)

package com.cmc12th.runway.ui.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Composable
fun HomeScreen(appState: ApplicationState) {

    appState.systmeUiController.setStatusBarColor(Color.Transparent)

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
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                count = 6,
            ) { page ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .fillMaxSize()
                ) {
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
                            IconButton(modifier = Modifier
                                .size(26.dp),
                                onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_border_bookmark_24),
                                    contentDescription = "IC_BORDER_BOOKMARK",
                                    tint = White
                                )
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
                                    text = "ATHPLATFPORM",
                                    style = TextStyle.Default.copy(
                                        fontSize = 26.sp,
                                        fontFamily = blackHanSans
                                    ),
                                    color = Primary
                                )
                                Text(
                                    text = "ATHPLATFPORM",
                                    style = TextStyle.Default.copy(
                                        fontSize = 26.sp,
                                        fontFamily = blackHanSans,
                                        drawStyle = Stroke(
                                            miter = 10f,
                                            width = 5f,
                                            join = StrokeJoin.Round,
                                        ),
                                    ),
                                    color = Color.White
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
                                    text = "성수동, 서울",
                                    color = Color.White,
                                    style = Body2M,
                                )
                            }


                            Text(
                                text = "# 페미닌 # 미니멀 # 시티보이",
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

            /** Banner Top */
            MainBannerTopBar()
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            text = "비슷한 취향의 사용자 후기",
            style = HeadLine4,
            color = Color.Black
        )

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
private fun BoxScope.MainBannerTopBar() {
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
            Text(text = "[닉네임]님의\n취향을 가득 담은 매장", style = HeadLine4, color = White)
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