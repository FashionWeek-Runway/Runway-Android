package com.cmc12th.runway.data.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegionSearch(
    @Json(name = "address")
    val address: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "regionId")
    val regionId: Int
)