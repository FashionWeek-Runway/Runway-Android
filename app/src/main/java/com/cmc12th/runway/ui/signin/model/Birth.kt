package com.cmc12th.runway.ui.signin.model

data class Birth(
    val date: String
) {
    fun checkValidation(): Boolean = date.length == BIRTH_LENGTH

    companion object {
        const val BIRTH_LENGTH = 8
        fun default(): Birth = Birth("")
    }
}