package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.cmc12th.runway.data.response.user.StoreMetaDataItem

@Composable
fun ColumnScope.BookmarkStoreContainer(
    bookmarkedStore: LazyPagingItems<StoreMetaDataItem>,
    navigateToDetail: (id: Int, storeName: String) -> Unit,
) {
    if (bookmarkedStore.itemCount == 0) {
        EmptyBookmarkStorage("마음에 드는 매장을 저장해 보세요")
    } else {
        BookmarkedStore(
            bookmarkedStore = bookmarkedStore,
            navigateToDetail = navigateToDetail
        )
    }
}