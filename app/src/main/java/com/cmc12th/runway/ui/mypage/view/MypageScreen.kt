package com.cmc12th.runway.ui.mypage.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.detail.view.DetailScreen
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.ReviewViwerType
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.runway.ui.mypage.components.*
import com.cmc12th.runway.ui.mypage.model.MypageTabInfo
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Gray50
import com.cmc12th.runway.ui.theme.HeadLine4
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.EDIT_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SETTING_GRAPH
import com.cmc12th.runway.utils.viewLogEvent
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun MypageScreen(appState: ApplicationState) {

    val viewModel: MypageViewModel = hiltViewModel()
    val state = rememberCollapsingToolbarScaffoldState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val myReviews = uiState.myReviews.collectAsLazyPagingItems()
    val bookmarkedStore = uiState.bookmarkedStore.collectAsLazyPagingItems()
    val profileImageUiState = viewModel.profileImageUiState.collectAsStateWithLifecycle()

    appState.systmeUiController.setStatusBarColor(Gray50)

    LaunchedEffect(key1 = Unit) {
        viewLogEvent("MypageScreen")
        viewModel.getBookmarkedStore()
        viewModel.getMyProfile()
        viewModel.getMyReviews()
    }

    CollapsingToolbarScaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = BOTTOM_NAVIGATION_HEIGHT)
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Column(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .parallax(1f)
            ) {
                /** 프로필 정보 */
                MainProfileInfo(
                    nickName = profileImageUiState.value.nickName.text,
                    profileImage = profileImageUiState.value.profileImage,
                    navigateToEditProfile = {
                        appState.navigate(EDIT_PROFILE_IMAGE_ROUTE)
                    }
                )
                HeightSpacer(height = 34.dp)
                /** 나의 후기, 저장 로우탭 */
                MypageCustomRowTab(
                    selectedPage = uiState.selectedPage
                ) {
                    viewModel.updateSelectedPage(it)
                }
            }
            TopBar { appState.navigate(SETTING_GRAPH) }
        }
    ) {
        Column {
            when (uiState.selectedPage) {
                MypageTabInfo.MY_REVIEW -> {
                    when (myReviews.loadState.refresh) {
                        is LoadState.Error -> {
                            EmptyMyReview("네트워크 연결을 확인해주세요.")
                        }
                        LoadState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.TopCenter)
                                        .padding(top = 100.dp),
                                    color = Primary,
                                    strokeWidth = 4.dp,
                                )
                            }

                        }
                        is LoadState.NotLoading -> {
                            if (myReviews.itemCount == 0) {
                                EmptyMyReview("내 스타일의 매장에 방문하고\n기록해보세요!")
                            } else {
                                MyReviews(
                                    navigateToUserReviewDetail = { index ->
                                        appState.navigate("${Constants.REVIEW_DETAIL_ROUTE}?reviewId=${index}&viewerType=${ReviewViwerType.MYPAGE.typeToString}")
                                    },
                                    myReviews = myReviews
                                )
                            }
                        }
                    }
                }
                MypageTabInfo.STORAGE -> {
                    if (bookmarkedStore.itemCount == 0) {
                        EmptyStorage()
                    } else {
                        BookmarkedStore(bookmarkedStore = bookmarkedStore,
                            navigateToDetail = { id, storeName ->
                                viewModel.updateOnDetail(DetailState(true, id, storeName))
                            })
                    }
                }
            }
        }
    }

    /** 디테일 뷰 위에 깔아버리기 */
    if (!uiState.onDetail.isDefault()) {
        DetailScreen(appState = appState,
            idx = uiState.onDetail.id,
            storeName = uiState.onDetail.storeName,
            onBackPress = {
                appState.bottomBarState.value = true
                appState.systmeUiController.setSystemBarsColor(color = Color.White)
                viewModel.updateOnDetail(DetailState.default())
            }
        )
    }
}


@Composable
private fun CollapsingToolbarScope.TopBar(
    navigateToSettingGraph: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
            .pin(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "MY", style = HeadLine4, color = Color.Black)
        RunwayIconButton(
            drawable = R.drawable.ic_baseline_setting_24,
            tint = Black
        ) {
            navigateToSettingGraph()
        }
    }
}




