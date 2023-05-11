package com.cmc12th.domain.model.signin

data class Birth(
    val date: String
) {
    fun checkValidation(): Boolean = date.length == BIRTH_LENGTH

    companion object {
        const val BIRTH_LENGTH = 8
        fun default(): Birth = Birth("")
    }
}