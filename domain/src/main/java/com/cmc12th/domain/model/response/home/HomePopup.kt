package com.cmc12th.domain.model.response.home

class HomePopup : ArrayList<HomePopUpItem>()

data class HomePopUpItem(
    val imgUrl: String,
    val popUpId: Int,
    val userId: Int
)

