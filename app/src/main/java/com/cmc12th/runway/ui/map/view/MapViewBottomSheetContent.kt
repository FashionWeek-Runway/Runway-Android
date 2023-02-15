package com.cmc12th.runway.ui.map.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE

@Composable
fun MapViewBottomSheetContent(appState: ApplicationState, screenHeight: Dp) {
    val shopList = remember {
        mutableStateOf(listOf("매장1", "매장2", "매장3", "매장4"))
//        mutableStateOf(emptyList<String>())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(screenHeight - 130.dp)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = BOTTOM_NAVIGATION_HEIGHT
            )
    ) {
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .height(3.dp)
                .width(36.dp)
                .background(Gray200)
                .align(Alignment.CenterHorizontally)
        )
        HeightSpacer(height = 10.dp)
        Text(
            text = "[성수동] 둘러보기", style = Body1M, color = Color.Black,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (shopList.value.isNotEmpty()) {
            LazyColumn {
                items(shopList.value) {
                    Column(modifier = Modifier.clickable {
                        appState.navigate("$DETAIL_ROUTE?idx=1")
//                        appState.navigate("${Constants.SIGNIN_PROFILE_IMAGE_ROUTE}?profileImage=$profileImage&kakaoId=$kakaoId")
                    }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.6f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_dummy),
                                contentDescription = "SHOP_IMAGE",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Row(modifier = Modifier.align(Alignment.BottomStart)) {
                                (0..2).map {
                                    Text(
                                        text = "태그명",
                                        style = Button2,
                                        color = Gray700,
                                        modifier = Modifier
                                            .padding(8.dp, 5.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White)
                                    )
                                }
                            }
                        }
                        Text(
                            text = it,
                            style = HeadLine4,
                            modifier = Modifier.padding(top = 14.dp, bottom = 30.dp)
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_DUMMY",
                    modifier = Modifier.size(100.dp)
                )
                HeightSpacer(height = 30.dp)
                Text(text = "아직 등록된 매장이 없습니다.", style = Body1, color = Black)
                Text(text = "위치를 이동하거나 필터를 변경해보세요.", style = Body2, color = Gray500)
            }
        }

    }
}
