@file:OptIn(
    ExperimentalTextApi::class, ExperimentalPagerApi::class,
    ExperimentalGlideComposeApi::class
)

package com.cmc12th.runway.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.home.model.HomeBannertype
import com.cmc12th.runway.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun BoxScope.HomeBannerComponents(
    homeBanners: MutableList<HomeBannertype>,
    updateBookmark: (storeId: Int, bookmarked: Boolean) -> Unit,
    navigateToDetail: (storeId: Int, storeName: String) -> Unit,
    navigateToShowMoreStore: () -> Unit,
) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .background(Gray200)
            .fillMaxSize(),
        count = homeBanners.size,
    ) { page ->

        when (val banner = homeBanners[page]) {
            HomeBannertype.SHOWMOREBANNER -> {
                if (homeBanners.size > 1) {
                    ShowMoreBanner(navigateToShowMoreStore)
                }
            }
            is HomeBannertype.STOREBANNER -> {
                HomeStoreBanner(
                    navigateToDetail = navigateToDetail,
                    banner = banner,
                    updateBookmark = updateBookmark
                )
            }
        }
    }

    if (pagerState.currentPage + 1 <= homeBanners.size) {
        HomeBannerStep(
            modifier = Modifier.align(Alignment.BottomCenter),
            step = pagerState.currentPage + 1,
            maxSize = homeBanners.size
        )
    }
}

@Composable
private fun ShowMoreBanner(navigateToShowMoreStore: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxSize()
            .clickable {
                navigateToShowMoreStore()
            }
    ) {

        /** 배경 이미지 */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black),
        )

        /** 하얀색 네모 박스 안 */
        Box(
            modifier = Modifier
                .padding(top = 130.dp, start = 39.dp, end = 39.dp, bottom = 20.dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.TopCenter)
                .border(
                    BorderStroke(
                        1.dp, brush = Brush.horizontalGradient(
                            listOf(Blue600, Blue900)
                        )
                    ), RoundedCornerShape(4.dp)
                )
        ) {

            Image(
                painter = painterResource(id = R.mipmap.img_home_banner_show_more_store),
                contentDescription = "IMG_HOME_BANNER_SHOW_MORE_STORE",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .padding(top = 11.dp)
                    .align(Alignment.TopCenter)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Black)
                    .border(BorderStroke(1.dp, White), CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SHOW\nMORE\nSHOP",
                    style = TextStyle.Default.copy(
                        fontSize = 24.sp,
                        fontFamily = blackHanSans,
                    ),
                    color = Point,
                    textAlign = TextAlign.Center
                )
                HeightSpacer(height = 28.dp)
                Image(
                    painter = painterResource(id = R.mipmap.img_runway_barcord),
                    contentDescription = "IMG_RUNWAY_BARCORD",
                    modifier = Modifier.size(63.dp, 21.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeStoreBanner(
    navigateToDetail: (storeId: Int, storeName: String) -> Unit,
    banner: HomeBannertype.STOREBANNER,
    updateBookmark: (storeId: Int, bookmarked: Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxSize()
            .clickable {
                navigateToDetail(banner.storeId, banner.storeName)
            }
    ) {

        /** 배경 이미지 */
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray200),
            model = banner.imgUrl,
            contentDescription = "SHOP_IMAGE",
            contentScale = ContentScale.Crop,
        ) {
            it.signature(ObjectKey(banner.imgUrl))
        }

        /** 배경 깔기 */
        Box(
            modifier = Modifier
                .background(Black10)
                .fillMaxSize()
        )

        /** 하얀색 네모 박스 안 */
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
                if (banner.bookmark) {
                    IconButton(modifier = Modifier
                        .size(26.dp),
                        onClick = {
                            updateBookmark(banner.storeId, false)
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
                            updateBookmark(banner.storeId, true)
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
                        text = banner.storeName,
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
                        text = banner.storeName,
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
                        text = banner.regionInfo,
                        color = Color.White,
                        style = Body2M,
                    )
                }

                Row(
                    modifier = Modifier.background(Primary),
                ) {
                    banner.categoryList.forEach {
                        Text(
                            text = "#$it",
                            color = Point,
                            style = Button2,
                            modifier = Modifier
                                .padding(2.dp, 4.dp)
                        )
                    }
                }

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
