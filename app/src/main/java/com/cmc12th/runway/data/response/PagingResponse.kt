package com.cmc12th.runway.data.response

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PagingResponse<out T>(
    @Json(name = "code")
    val code: String,
    @Json(name = "isSuccess")
    val isSuccess: Boolean,
    @Json(name = "message")
    val message: String,
    @SerializedName("result")
    val pagingMetadata: PagingMetadata<T>
)