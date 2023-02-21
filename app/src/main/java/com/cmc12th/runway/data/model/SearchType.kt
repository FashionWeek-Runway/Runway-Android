package com.cmc12th.runway.data.model

//sealed class SearchType(val id: Int) {
//    class LOCATION(id: Int) : SearchType(id)
//    class STORE(id: Int) : SearchType(id)
//}

data class SearchType(
    val id: Int,
    val type: String,
) {
    companion object {
        const val LOCATION_TYPE = "LOCATION"
        const val STORE_TYPE = "STORE"
    }
}