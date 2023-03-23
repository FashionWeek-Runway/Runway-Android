package com.cmc12th.runway.ui.signin.model

data class Nickname(
    val text: String,
) {

    fun checkValidate() = regex.matches(text)

    companion object {
        private val regex = Regex("""^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{2,10}$""")
        const val MAX_NICKNAME_LENGTH = 10

        fun default() = Nickname("")
    }
}