package com.cmc12th.runway.ui.signin

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants.CATEGORYS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
) : ViewModel() {

    private val tagLists = CATEGORYS.map {
        CategoryTag(it)
    }.toTypedArray()

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

    private val _password = mutableStateOf(Password.default())
    val password: State<Password> = _password

    private val _retryPassword = mutableStateOf(Password.default())
    val retryPassword: State<Password> = _retryPassword

    private val _verifyCode = mutableStateOf("")
    val verifyCode: State<String> = _verifyCode

    private val _nickName = mutableStateOf("")
    val nickName: State<String> = _nickName

    private val _profileImage = mutableStateOf<Uri?>(null)
    val profileImage: State<Uri?> = _profileImage

    val agreements = mutableStateListOf(false, false, false)
    val categoryTags = mutableStateListOf(*tagLists)


    fun updateCategoryTags(categoryTag: CategoryTag) {
        for (index in 0 until categoryTags.size)
            if (categoryTags[index].name == categoryTag.name) {
                categoryTags[index] = categoryTags[index].copy(
                    isSelected = !categoryTags[index].isSelected
                )
            }
    }

    fun updateProfileImage(profileImage: Uri?) {
        _profileImage.value = profileImage
    }

    fun updateNickName(nickname: String) {
        _nickName.value = nickname
    }

    fun updateVerifyCode(verifyCode: String) {
        _verifyCode.value = verifyCode
    }

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