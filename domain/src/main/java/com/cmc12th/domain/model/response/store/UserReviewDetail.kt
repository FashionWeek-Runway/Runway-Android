package com.cmc12th.domain.model.response.store

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReviewDetail(
    val bookmark: Boolean = false,
    val bookmarkCnt: Int = 0,
    val imgUrl: String? = "",
    val my: Boolean = false,
    val nickname: String = "",
    val profileImgUrl: String? = "",
    val regionInfo: String = "",
    val reviewId: Int = -1,
    val reviewInquiry: ReviewInquiry = ReviewInquiry(null, null, null, null),
    val storeId: Int = -1,
    val storeName: String = "",
) {
    fun isDefault(): Boolean = this.reviewId == -1 && this.reviewId == -1

    companion object {
        fun default() = UserReviewDetail(storeId = -1, reviewId = -1)
    }
}