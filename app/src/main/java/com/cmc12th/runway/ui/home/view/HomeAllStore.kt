package com.cmc12th.runway.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.home.HomeViewModel
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.DETAIL_ROUTE

@Composable
fun HomeAllStore(
    appState: ApplicationState,
    viewModel: HomeViewModel,
) {

//    val allStores = viewModel.allStores.collectAsLazyPagingItems()
    val navigateToDetail: (storeId: Int, storeName: String) -> Unit = { storeId, storeName ->
        appState.navigate(
            "${DETAIL_ROUTE}?storeId=$storeId&storeName=$storeName"
        )
    }
    appState.systmeUiController.setStatusBarColor(Color.White)
    LaunchedEffect(key1 = Unit) {
        viewModel.getHomeBanner(1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.navController.popBackStack()
            }
            Text(text = "취향을 가득 담은 매장", style = Body1B)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(viewModel.allStores) { store ->
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navigateToDetail(
                            store.storeId,
                            store.storeName,
                        )
                    }) {

                    AsyncImage(
                        modifier = Modifier
                            .background(Gray200)
                            .aspectRatio(0.75f)
                            .fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(store.imgUrl)
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "IMG_PROFILE",
                        contentScale = ContentScale.Crop,
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Black10))

                    if (!store.bookmark) {
                        IconButton(
                            onClick = {
                                viewModel.updateBookmark(store.storeId) {
                                    viewModel.updateBookmarkState(store.storeId, !store.bookmark)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 12.dp, end = 8.dp)
                                .size(26.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_border_bookmark_24),
                                contentDescription = "IC_BORDER_BOOKMARK",
                                tint = Color.White
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                viewModel.updateBookmark(store.storeId) {
                                    viewModel.updateBookmarkState(store.storeId, !store.bookmark)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 12.dp, end = 8.dp)
                                .size(26.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filled_bookmark_24),
                                contentDescription = "IC_FILLED_BOOKMARK",
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = store.storeName,
                            style = Body2B,
                            color = White)
                        HeightSpacer(height = 2.dp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filled_review_location_16),
                                contentDescription = "IC_LOCATION",
                                modifier = Modifier.size(12.dp),
                                tint = Color.Unspecified
                            )
                            Text(text = store.regionInfo,
                                style = Caption,
                                color = Gray50)
                        }
                        HeightSpacer(height = 6.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            store.categoryList.forEach {
                                Text(text = "#$it",
                                    style = Caption2,
                                    color = Gray50)
                            }
                        }
                    }
                }
            }
        }

    }
}