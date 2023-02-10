package com.cmc12th.runway.ui.signin.model

data class Password(
    val value: String,
) {

    fun includeEnglish(): Boolean =
        value.any { ('a'..'z').contains(it) || ('A'..'Z').contains(it) }

    fun includeNumber(): Boolean =
        value.any { ('1'..'9').contains(it) }

    fun inLegnth(): Boolean = value.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH

    fun isValidatePassword(password: Password): Boolean =
        password.includeEnglish() && password.includeNumber() && password.inLegnth() && password.value == this.value

    companion object {

        fun default() = Password("")

        private const val MIN_PASSWORD_LENGTH = 8
        private const val MAX_PASSWORD_LENGTH = 16
    }
}