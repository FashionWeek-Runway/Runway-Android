package com.cmc12th.domain.model.signin

enum class Gender(val text: String) {
    Male("남"), FeMale("여"), Unknown("");

    fun isMale(): Boolean = this == Male
    fun isFemale(): Boolean = this == FeMale
    fun isNotUnknown(): Boolean = this != Unknown
}