package com.cmc12th.runway.data.response.store


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImgUrlAndNicknameAndCategorys(
    @Json(name = "categoryList")
    val categoryList: List<String>,
    @Json(name = "imgUrl")
    val imgUrl: String?,
    @Json(name = "nickname")
    val nickname: String
)