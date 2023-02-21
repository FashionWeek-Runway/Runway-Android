package com.cmc12th.runway.ui.map.model

enum class MapStatus {
    DEFAULT, ZOOM, LOCATION_SEARCH, LOCATION_SEARCH_MARKER_CLICKED, MARKER_CLICKED, SHOP_SEARCH, SEARCH_TAB, SEARCH_ZOOM;

    fun searchResultTopBarVisiblity(): Boolean =
        this == SHOP_SEARCH || this == SEARCH_ZOOM || this == LOCATION_SEARCH

    fun isGpsIconVisibility(): Boolean =
        this == ZOOM || this == SEARCH_ZOOM

}