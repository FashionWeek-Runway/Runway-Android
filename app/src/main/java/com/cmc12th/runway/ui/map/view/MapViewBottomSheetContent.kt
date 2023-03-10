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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
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
    isExpandedTagetValue: Boolean,
    setMapStatusOnSearch: () -> Unit,
    setMapStatusDefault: () -> Unit,
    contents: BottomSheetContent,
    navigateToDetail: (id: Int, storeName: String) -> Unit,
    isExpanded: Boolean,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
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
            visible = isFullScreen && isExpandedTagetValue,
            enter = fadeIn(),
        ) {
            SearchResultBar(
                modifier = Modifier.padding(0.dp, top = 0.dp, bottom = 16.dp),
                setMapStatusDefault = setMapStatusDefault,
                setMapStatusOnSearch = setMapStatusOnSearch,
                bottomSheetContent = contents
            )
        }

        /** ??????????????? ????????? ??????????????? ????????? ????????? ?????????. */
        if (!(isFullScreen && isExpandedTagetValue)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

            /** ?????? */
            when (contents) {
                BottomSheetContent.DEFAULT -> {}
                BottomSheetContent.LOADING -> {}
                is BottomSheetContent.MULTI -> {
                    HeightSpacer(height = 10.dp)
//                    Text(
//                        text = contents.locationName, style = Body1M, color = Color.Black,
//                        modifier = Modifier.padding(bottom = 20.dp)
//                    )
                }
                is BottomSheetContent.SINGLE -> {
                    HeightSpacer(height = 10.dp)
                }
            }
        }

        /** ?????? ?????? */
        when (contents) {
            BottomSheetContent.DEFAULT -> {
                MapBottomSheetEmptyStore()
            }
            is BottomSheetContent.MULTI -> {
                val pagingContents = contents.contents.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier.pagingHeight(isExpanded, screenHeight),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(pagingContents) {
                        it?.let {
                            BottomDetailItem(navigateToDetail, it)
                        }
                    }
                }
            }
            is BottomSheetContent.SINGLE -> {
                BottomDetailItem(navigateToDetail, contents.contents)
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
        Text(text = "?????? ????????? ????????? ????????????.", style = Body1, color = Black)
        Text(text = "????????? ??????????????? ????????? ??????????????????.", style = Body2, color = Gray500)
    }
}

private fun Modifier.pagingHeight(isExpanded: Boolean, peekHeight: Dp): Modifier {
    return if (isExpanded) {
        Modifier.wrapContentHeight()
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
