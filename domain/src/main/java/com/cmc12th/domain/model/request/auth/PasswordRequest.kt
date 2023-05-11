package com.cmc12th.domain.model.request.auth


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordRequest(
    @Json(name = "password")
    val password: String
)