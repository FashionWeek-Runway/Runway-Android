package com.cmc12th.runway.ui.home.model

import com.cmc12th.runway.data.response.home.HomeBannerItem

sealed class HomeBannertype {
    data class STOREBANNER(
        val bookmark: Boolean,
        val bookmarkCnt: Int,
        val categoryList: List<String>,
        val imgUrl: String,
        val regionInfo: String,
        val storeId: Int,
        val storeName: String,
    ) : HomeBannertype()

    object SHOWMOREBANNER : HomeBannertype()

    companion object {
        fun toStoreBanner(homeBannerItem: HomeBannerItem): STOREBANNER {
            return STOREBANNER(
                bookmark = homeBannerItem.bookmark,
                bookmarkCnt = homeBannerItem.bookmarkCnt,
                categoryList = homeBannerItem.categoryList,
                imgUrl = homeBannerItem.imgUrl,
                regionInfo = homeBannerItem.regionInfo,
                storeId = homeBannerItem.storeId,
                storeName = homeBannerItem.storeName
            )
        }
    }
}