package com.cmc12th.runway.data.response.user


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInformationManagamentInfo(
    @Json(name = "apple")
    val apple: Boolean,
    @Json(name = "kakao")
    val kakao: Boolean,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "social")
    val social: Boolean
)