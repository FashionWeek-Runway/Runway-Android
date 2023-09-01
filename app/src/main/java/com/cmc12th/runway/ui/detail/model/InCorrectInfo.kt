package com.cmc12th.runway.ui.detail.model

data class InCorrectInfo(
    val name: String,
    val id: Int,
) {
    companion object {
        val default = listOf(
            InCorrectInfo("주소가 올바르지 않아요", 1),
            InCorrectInfo("영업 시간이 올바르지 않아요", 2),
            InCorrectInfo("전화번호가 올바르지 않아요", 3),
            InCorrectInfo("인스타그램이 연결되지 않아요", 4),
            InCorrectInfo("홈페이지가 연결되지 않아요", 5),
        )
    }
}
