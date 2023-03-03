package com.cmc12th.runway.data.response.home

data class HomeBannerItem(
    val bookmark: Boolean,
    val bookmarkCnt: Int,
    val categoryList: List<String>,
    val imgUrl: String,
    val regionInfo: String,
    val storeId: Int,
    val storeName: String
)