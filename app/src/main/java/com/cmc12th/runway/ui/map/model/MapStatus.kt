package com.cmc12th.runway.ui.map.model

enum class MapStatus {
    DEFAULT, ZOOM, LOCATION_SEARCH, SHOP_SEARCH, SEARCH_TAB, SEARCH_ZOOM;

    fun searchResultTopBarVisiblity(): Boolean =
        this == SHOP_SEARCH || this == SEARCH_ZOOM || this == LOCATION_SEARCH

}