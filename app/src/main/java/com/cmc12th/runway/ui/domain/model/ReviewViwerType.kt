package com.cmc12th.runway.ui.domain.model

enum class ReviewViwerType(val typeToString: String) {
    STORE_DETAIL("STORE_DETAIL"), MYPAGE("MYPAGE"), HOME("HOME"), BOOKMARK("BOOKMARK");

    companion object {
        fun convertStringToEnum(stringType: String): ReviewViwerType = values().find {
            it.typeToString == stringType
        } ?: throw IllegalArgumentException("[ERROR] 해당 타입의 리뷰는 존재하지 않아요~")
    }
}
