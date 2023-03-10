package com.cmc12th.runway.ui.mypage.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.cmc12th.runway.ui.mypage.model.MypageBookmarkTabInfo
import com.cmc12th.runway.ui.mypage.model.MypageTabInfo
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.EDIT_PROFILE_IMAGE_ROUTE
import com.cmc12th.runway.utils.Constants.REVIEW_DETAIL_ROUTE
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
    val bookmarkedReview = uiState.bookmarkedReview.collectAsLazyPagingItems()
    val profileImageUiState = viewModel.profileImageUiState.collectAsStateWithLifecycle()

    appState.systmeUiController.setStatusBarColor(Gray50)

    LaunchedEffect(key1 = Unit) {
        viewLogEvent("MypageScreen")
        viewModel.getBookmarkedStore()
        viewModel.getBookmarkedReview()
        viewModel.getMyReviews()
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
                /** ????????? ?????? */
                MainProfileInfo(
                    nickName = profileImageUiState.value.nickName.text,
                    profileImage = profileImageUiState.value.profileImage,
                    navigateToEditProfile = {
                        appState.navigate(EDIT_PROFILE_IMAGE_ROUTE)
                    }
                )
                HeightSpacer(height = 34.dp)
                /** ?????? ??????, ?????? ????????? */
                MypageCustomRowTab(
                    selectedPage = uiState.selectedPage,
                    updateSelectedPage = {
                        viewModel.updateSelectedPage(it)
                    }
                )
            }
            TopBar { appState.navigate(SETTING_GRAPH) }
        }
    ) {
        Column {
            when (uiState.selectedPage) {
                MypageTabInfo.MY_REVIEW -> {
                    MyreviewsContainer(
                        myReviews = myReviews,
                        navigateToUserReviewDetail = { index ->
                            appState.navigate("${REVIEW_DETAIL_ROUTE}?reviewId=${index}&viewerType=${ReviewViwerType.MYPAGE.typeToString}")
                        }
                    )
                }
                MypageTabInfo.STORAGE -> {
                    MypageBookmarkRowTab(
                        selectedPage = uiState.selectedBookmarkPage,
                        updateSelectedPage = {
                            viewModel.updateSelectedBookmarkPage(it)
                        }
                    )
                    when (uiState.selectedBookmarkPage) {
                        MypageBookmarkTabInfo.STORE -> {
                            BookmarkStoreContainer(
                                bookmarkedStore = bookmarkedStore,
                                navigateToDetail = { id, storeName ->
                                    viewModel.updateOnDetail(DetailState(true, id, storeName))
                                }
                            )
                        }
                        MypageBookmarkTabInfo.REVIEW -> {
                            BookmarkReviewContainer(
                                bookmarkedReview = bookmarkedReview,
                                navigateToUserReviewDetail = { index ->
                                    appState.navigate("${REVIEW_DETAIL_ROUTE}?reviewId=${index}&viewerType=${ReviewViwerType.BOOKMARK.typeToString}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    /** ????????? ??? ?????? ??????????????? */
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




