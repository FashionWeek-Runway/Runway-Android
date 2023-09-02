package com.cmc12th.domain.model.response.home

data class HomeInstaResponse(
    val feedId: Int,
    val imgList: List<String>,
    val instaLink: String,
    val storeName: String,
    val description: String
)