package com.cmc12th.runway.ui.signin.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class NicknameTest {

    private val regex = Regex("""^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{1,10}$""")

    @ParameterizedTest
    @CsvSource(value = ["합격닉네임:true", "특수문자닉네임@:false", "10자가넘어가버리는닉네임ㅇㅇㅇ:false", ".:false", "a.:false", "a:true"],
        delimiter = ':')
    @DisplayName("이름 정규식을 만들기 위한 테스트")
    fun regexTest(nickname: String = "", result: Boolean) {
        assertThat(regex.matches(nickname)).isEqualTo(result)
    }

    @Test
    @DisplayName("공백 닉네임 테스트")
    fun blankRegexTest() {
        assertThat(regex.matches("")).isFalse
    }

}