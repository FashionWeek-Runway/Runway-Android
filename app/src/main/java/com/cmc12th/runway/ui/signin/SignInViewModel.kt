package com.cmc12th.runway.ui.signin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.cmc12th.runway.ui.signin.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
) : ViewModel() {

    private val _nameAndNationality =
        mutableStateOf<NameAndNationality>(NameAndNationality.default())
    val nameAndNationality: State<NameAndNationality> = _nameAndNationality

    private val _gender = mutableStateOf(Gender.Unknown)
    val gender: State<Gender> = _gender

    private val _birth = mutableStateOf(Birth.default())
    val birth: State<Birth> = _birth

    private val _phone = mutableStateOf(Phone.default())
    val phone: State<Phone> = _phone

    private val _userVerificationStatus = mutableStateOf(false)
    val userVerificationStatus: State<Boolean> = _userVerificationStatus

    private val _password = mutableStateOf(Password(""))
    val password: State<Password> = _password

    private val _retryPassword = mutableStateOf(Password(""))
    val retryPassword: State<Password> = _retryPassword

    fun updatePassword(password: Password) {
        _password.value = password
    }

    fun updateRetryPassword(password: Password) {
        _retryPassword.value = password
    }

    fun updateName(name: String) {
        _nameAndNationality.value = _nameAndNationality.value.copy(name = name)
        checkUserVerificationStatus()
    }

    fun updateNationality(nationality: Nationality) {
        _nameAndNationality.value = _nameAndNationality.value.copy(nationality = nationality)
    }

    fun updateGender(gender: Gender) {
        _gender.value = gender
        checkUserVerificationStatus()
    }

    fun updateBirth(birth: Birth) {
        _birth.value = birth
        checkUserVerificationStatus()
    }

    fun updateMobileCarrier(mobileCarrier: MobileCarrier) {
        _phone.value = _phone.value.copy(mobileCarrier = mobileCarrier)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _phone.value = _phone.value.copy(number = phoneNumber)
        checkUserVerificationStatus()
    }


    private fun checkUserVerificationStatus() {
        val isVerified =
            _gender.value.isNotUnknown() && _nameAndNationality.value.name.isNotBlank() && _birth.value.checkValidation() && _phone.value.checkValidation()
        _userVerificationStatus.value = isVerified
    }

}