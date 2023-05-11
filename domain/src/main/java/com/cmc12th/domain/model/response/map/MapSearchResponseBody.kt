package com.cmc12th.domain.model.response.map


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MapSearchResponseBody(
    @Json(name = "regionSearchList")
    val regionSearchList: List<RegionSearch>,
    @Json(name = "storeSearchList")
    val storeSearchList: List<StoreSearch>
)