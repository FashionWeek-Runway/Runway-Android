package com.cmc12th.runway.ui.signin.model

enum class MobileCarrier(private val krName: String) {
    SKT("SKT"), KT("KT"), LG("LG+"),
    FRUGAL_SKT("SKT 알뜰폰"), FRUGAL_KT("KT 알뜰폰"), FRUGAL_LG("LG+ 알뜰폰");

    fun getName(): String {
        return this.krName
    }
}