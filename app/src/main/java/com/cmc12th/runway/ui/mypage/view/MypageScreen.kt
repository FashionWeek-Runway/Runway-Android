package com.cmc12th.runway.ui.mypage.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.EDIT_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.SETTING_GRAPH
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

enum class MypageTabInfo(val id: Int) {
    MY_REVIEW(1), STORAGE(2);
}

@Composable
fun MypageScreen(appState: ApplicationState) {

    val viewModel: MypageViewModel = hiltViewModel()
    val myReviews = viewModel.myReviews.collectAsLazyPagingItems()
    val bookmarkedStore = viewModel.bookmarkedStore.collectAsLazyPagingItems()
    val state = rememberCollapsingToolbarScaffoldState()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val profileImageUiState = viewModel.profileImageUiState.collectAsStateWithLifecycle()

    appState.systmeUiController.setStatusBarColor(Gray50)

    LaunchedEffect(key1 = Unit) {
        viewModel.getMyReviews()
        viewModel.getBookmarkedStore()
        viewModel.getMyProfile()
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
                    selectedPage = uiState.value.selectedPage
                ) {
                    viewModel.updateSelectedPage(it)
                }
            }
            TopBar { appState.navigate(SETTING_GRAPH) }
        }
    ) {
        when (uiState.value.selectedPage) {
            MypageTabInfo.MY_REVIEW -> {
                Column {
                    if (myReviews.itemCount == 0) {
                        EmptyMyReview()
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
            MypageTabInfo.STORAGE -> {
                Column {
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
    if (!uiState.value.onDetail.isDefault()) {
        DetailScreen(appState = appState,
            idx = uiState.value.onDetail.id,
            storeName = uiState.value.onDetail.storeName,
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




