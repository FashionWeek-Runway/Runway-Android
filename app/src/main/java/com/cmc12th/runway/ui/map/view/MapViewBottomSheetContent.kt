package com.cmc12th.runway.ui.map.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.map.components.BottomDetailItem
import com.cmc12th.domain.model.map.model.BottomSheetContent
import com.cmc12th.domain.model.map.model.MapStatus
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT

@Composable
fun MapViewBottomSheetContent(
    appState: ApplicationState,
    screenHeight: Dp,
    isFullScreen: Boolean,
    isExpandedTagetValue: Boolean,
    setMapStatusOnSearch: () -> Unit,
    setMapStatusDefault: () -> Unit,
    contents: BottomSheetContent,
    navigateToDetail: (id: Int, storeName: String) -> Unit,
    isExpanded: Boolean,
    mapStatus: MapStatus,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = if (appState.bottomBarState.value) BOTTOM_NAVIGATION_HEIGHT else 0.dp
            ),
    ) {

        AnimatedVisibility(
            visible = isFullScreen && isExpandedTagetValue,
            enter = fadeIn(),
        ) {
            SearchResultBar(
                modifier = Modifier
                    .padding(0.dp, top = 16.dp, bottom = 16.dp),
                setMapStatusDefault = setMapStatusDefault,
                setMapStatusOnSearch = setMapStatusOnSearch,
                bottomSheetContent = contents
            )
        }

        /** 풀스크린이 아니고 확장상태가 아니면 탑바가 보인다. */
        if (!(isFullScreen && isExpandedTagetValue)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .height(3.dp)
                        .width(36.dp)
                        .background(Gray200)
                        .align(Alignment.Center)
                )
            }

            /** 제목 */
            when (contents) {
                is BottomSheetContent.MULTI -> {
                    if (contents.locationName.isNotBlank()) {
                        Text(
                            text = "[${contents.locationName}] 둘러보기",
                            style = Body1M,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                }

                else -> {}
            }
        }

        /** 본문 내용 */
        when (contents) {
            BottomSheetContent.DEFAULT -> {
                MapBottomSheetEmptyStore()
            }

            is BottomSheetContent.MULTI -> {
                val pagingContents = contents.contents.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier
                        .pagingHeight(isExpanded, screenHeight),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
                ) {
                    items(pagingContents) {
                        it?.let {
                            BottomDetailItem(navigateToDetail, it)
                        }
                    }
                    if (pagingContents.itemCount == 0) {
                        item {
                            MapBottomSheetEmptyStore()
                        }
                    }
                }
            }

            is BottomSheetContent.SINGLE -> {
                // 마커 클릭상태일때만
                BottomDetailItem(
                    navigateToDetail = navigateToDetail,
                    mapInfoItem = contents.contents,
                    isNavigationButtonEnabled = mapStatus == MapStatus.MARKER_CLICKED
                )
            }

            BottomSheetContent.LOADING -> {
                MapBottomSheetEmptyStore()
            }
        }
    }
}

@Composable
private fun MapBottomSheetEmptyStore() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 100.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_store_empty),
            contentDescription = "IMG_STORE_EMPTY",
            modifier = Modifier.size(100.dp)
        )
        HeightSpacer(height = 30.dp)
        Text(text = "아직 등록된 매장이 없습니다.", style = Body1, color = Black)
        Text(text = "위치를 이동하거나 필터를 변경해보세요.", style = Body2, color = Gray500)
    }
}

private fun Modifier.pagingHeight(isExpanded: Boolean, peekHeight: Dp): Modifier {
    return if (isExpanded) {
        Modifier.fillMaxHeight()
    } else {
        Modifier.height(peekHeight)
    }
}

private fun Modifier.isFullScreen(isFullScreen: Boolean, screenHeight: Dp): Modifier {
    return if (isFullScreen) {
        Modifier.fillMaxHeight()
    } else {
        Modifier.heightIn(max = screenHeight)
    }
}
