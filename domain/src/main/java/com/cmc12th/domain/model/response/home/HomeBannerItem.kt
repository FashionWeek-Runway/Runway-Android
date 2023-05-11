package com.cmc12th.domain.model.response.home

data class HomeBannerItem(
    var bookmark: Boolean,
    val bookmarkCnt: Int,
    val categoryList: List<String>,
    val imgUrl: String,
    val regionInfo: String,
    val storeId: Int,
    val storeName: String
)