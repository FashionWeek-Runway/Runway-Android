package com.cmc12th.runway.ui.map.view

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.components.BottomDetailItem
import com.cmc12th.runway.ui.map.model.BottomSheetContent
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT

@Composable
fun MapViewBottomSheetContent(
    appState: ApplicationState,
    screenHeight: Dp,
    isFullScreen: Boolean,
    isExpanded: Boolean,
    setMapStatusOnSearch: () -> Unit,
    setMapStatusDefault: () -> Unit,
    contents: BottomSheetContent,
    navigateToDetail: (id: Int, storeName: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .isFullScreen(isFullScreen, screenHeight)
            .wrapContentHeight()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = if (appState.bottomBarState.value) BOTTOM_NAVIGATION_HEIGHT * 2 else BOTTOM_NAVIGATION_HEIGHT
            )
    ) {
        AnimatedVisibility(
            visible = isFullScreen && isExpanded,
            enter = fadeIn(),
        ) {
            SearchResultBar(
                modifier = Modifier.padding(0.dp, top = 0.dp, bottom = 16.dp),
                setMapStatusDefault = setMapStatusDefault,
                setMapStatusOnSearch = setMapStatusOnSearch,
                bottomSheetContent = contents
            )
        }

        /** 풀스크린이 아니고 확장상태가 아니면 탑바가 보인다. */
        if (!(isFullScreen && isExpanded)) {
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
        }

        when (contents) {
            BottomSheetContent.DEFAULT -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 100.dp),
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
            is BottomSheetContent.MULTI -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(contents.getData()) {
                        BottomDetailItem(navigateToDetail, it)
                    }
                }
            }
            is BottomSheetContent.SINGLE -> {
                LazyColumn {
                    items(contents.getData()) {
                        Column(modifier = Modifier.clickable {
                            navigateToDetail(it.storeId, it.storeName)
                        }) {
                            BottomDetailItem(navigateToDetail, it)
                        }
                    }
                }
            }
            BottomSheetContent.LOADING -> {

            }
        }
    }
}


private fun Modifier.isFullScreen(isFullScreen: Boolean, screenHeight: Dp): Modifier {
    return if (isFullScreen) {
        Modifier.fillMaxHeight()
    } else {
        Modifier.heightIn(max = screenHeight)
    }
}
