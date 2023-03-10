package com.cmc12th.runway.ui.signin.model

enum class Nationality(private val string: String) {
    LOCAL("내국인"), FOREIGNER("외국인");

    fun getString(): String = string
}