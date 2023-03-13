package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.ui.theme.Primary

@Composable
fun ColumnScope.MyreviewsContainer(
    myReviews: LazyPagingItems<MyReviewsItem>,
    navigateToUserReviewDetail: (index: Int) -> Unit,
) {
    when (myReviews.loadState.refresh) {
        is LoadState.Error -> {
            EmptyStorage(
                title = "네트워크 연결을 확인해주세요.",
                drawableResId = R.mipmap.img_empty_myreview
            )
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
                EmptyStorage(
                    title = "내 스타일의 매장에 방문하고\n기록해보세요!",
                    drawableResId = R.mipmap.img_empty_myreview
                )
            } else {
                MyReviews(
                    navigateToUserReviewDetail = navigateToUserReviewDetail,
                    myReviews = myReviews
                )
            }
        }
    }
}
