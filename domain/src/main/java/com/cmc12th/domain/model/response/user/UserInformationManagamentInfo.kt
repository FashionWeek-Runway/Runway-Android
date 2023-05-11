package com.cmc12th.domain.model.response.user


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
    val social: Boolean,
) {
    companion object {
        fun default() = UserInformationManagamentInfo(
            apple = false,
            kakao = false, phone = "-", social = false
        )
    }
}