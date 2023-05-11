package com.cmc12th.domain.model.signin

data class Phone(
    val number: String,
    val mobileCarrier: MobileCarrier
) {
    fun checkValidation(): Boolean = number.length == PHONE_NUMBER_LENGTH

    companion object {
        const val PHONE_NUMBER_LENGTH = 11

        fun default(): Phone {
            return Phone("", MobileCarrier.SKT)
        }
    }
}

