package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.cmc12th.runway.data.response.map.MapInfoItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.ui.map.components.BottomDetailItem

@Composable
fun BookmarkedStore(bookmarkedStore: LazyPagingItems<StoreMetaDataItem>) {
    val navigateToDetail: (id: Int, storeName: String) -> Unit = { id, storeName ->
    }

    LazyColumn(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(bookmarkedStore) {
            it?.let {
                BottomDetailItem(
                    navigateToDetail = navigateToDetail,
                    it = MapInfoItem(
                        category = it.category,
                        storeId = it.storeId,
                        storeImg = it.storeImg ?: "",
                        storeName = it.storeName
                    )
                )
            }
        }
    }
}