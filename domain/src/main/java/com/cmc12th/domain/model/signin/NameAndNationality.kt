package com.cmc12th.domain.model.signin

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
