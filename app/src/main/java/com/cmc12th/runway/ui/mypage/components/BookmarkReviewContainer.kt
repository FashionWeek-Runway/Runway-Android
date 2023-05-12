package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.cmc12th.runway.R
import com.cmc12th.domain.model.response.user.MyReviewsItem

@Composable
fun ColumnScope.BookmarkReviewContainer(
    bookmarkedReview: LazyPagingItems<MyReviewsItem>,
    navigateToUserReviewDetail: (Int) -> Unit,
) {
    if (bookmarkedReview.itemCount == 0) {
        EmptyStorage(
            title = "마음에 드는 사용자 후기를 저장해보세요.",
            drawableResId = R.mipmap.img_empty_bookmared_review
        )
    } else {
        MyReviews(
            navigateToUserReviewDetail = navigateToUserReviewDetail,
            myReviews = bookmarkedReview
        )
    }
}