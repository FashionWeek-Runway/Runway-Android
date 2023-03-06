package com.cmc12th.runway.data.model

data class SearchType(
    val id: Int,
    val type: String,
) {
    companion object {
        const val LOCATION_TYPE = "LOCATION"
        const val STORE_TYPE = "STORE"
    }
}