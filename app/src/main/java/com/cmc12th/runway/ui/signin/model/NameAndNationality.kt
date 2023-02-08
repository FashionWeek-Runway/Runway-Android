package com.cmc12th.runway.ui.signin.model

data class NameAndNationality(
    val name: String,
    val nationality: Nationality
) {
    companion object {
        fun default(): NameAndNationality {
            return NameAndNationality("", Nationality.LOCAL)
        }
    }
}
