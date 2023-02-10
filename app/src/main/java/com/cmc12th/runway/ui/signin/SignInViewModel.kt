package com.cmc12th.runway.ui.signin

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants.CATEGORYS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


/** UserVerification 화면의 Ui State */
data class UserVerificationUiState(
    val nameAndNationality: NameAndNationality = NameAndNationality.default(),
    val gender: Gender = Gender.Unknown,
    val birth: Birth = Birth.default(),
    val phone: Phone = Phone.default(),
    val userVerificationStatus: Boolean = false,
)

data class PhoneVerifyUiState(
    val phone: Phone = Phone.default(),
    val verifyCode: String = "",
)

data class PasswordUiState(
    val password: Password = Password.default(),
    val retryPassword: Password = Password.default(),
) {
    fun checkValidate() = password.isValidatePassword(retryPassword)
}

data class AgreementUiState(
    val agreements: MutableList<Agreement> = mutableListOf(
        Agreement("약관1", false),
        Agreement("약관2", false),
        Agreement("약관3", false),
    ),
) {
    fun isAllChcked() = agreements.all { it.isChecked }
}

data class ProfileImageUiState(
    val profileImage: Uri? = null,
    val nickName: Nickname = Nickname.default(),
)


data class CategoryUiState(
    val nickName: Nickname = Nickname.default(),
    val categoryTags: MutableList<CategoryTag> = CATEGORYS.map {
        CategoryTag(it)
    }.toMutableList(),
) {
    fun anyCategorySelected() = categoryTags.any { it.isSelected }
}


@HiltViewModel
class SignInViewModel @Inject constructor(
) : ViewModel() {

    private val tagLists = CATEGORYS.map {
        CategoryTag(it)
    }.toTypedArray()

    private val _nameAndNationality = MutableStateFlow(NameAndNationality.default())
    private val _gender = MutableStateFlow(Gender.Unknown)
    private val _birth = MutableStateFlow(Birth.default())
    private val _phone = MutableStateFlow(Phone.default())
    private val _userVerificationStatus = MutableStateFlow(false)

    private val _verifyCode = MutableStateFlow("")

    private val _password = MutableStateFlow(Password.default())
    private val _retryPassword = MutableStateFlow(Password.default())

    private val _agreements = MutableStateFlow(mutableListOf(
        Agreement("약관1", false),
        Agreement("약관2", false),
        Agreement("약관3", false),
    ))

    private val _nickName = MutableStateFlow(Nickname.default())
    private val _profileImage = MutableStateFlow<Uri?>(null)

    private val _categoryTags = MutableStateFlow(CATEGORYS.map {
        CategoryTag(it)
    }.toMutableList())

    val userVerificationUiState: StateFlow<UserVerificationUiState> = combine(
        _nameAndNationality, _gender, _birth, _phone, _userVerificationStatus
    ) { nameAndNationality, gender, birth, phone, userVerificationStatus ->
        UserVerificationUiState(
            nameAndNationality = nameAndNationality,
            gender = gender,
            birth = birth,
            phone = phone,
            userVerificationStatus = userVerificationStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserVerificationUiState()
    )

    val phoneVerifyUiState: StateFlow<PhoneVerifyUiState> =
        combine(_phone, _verifyCode) { phone, verifyCode ->
            PhoneVerifyUiState(phone = phone, verifyCode = verifyCode)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PhoneVerifyUiState()
        )

    val passwordUiState: StateFlow<PasswordUiState> =
        combine(_password, _retryPassword) { password, retryPassword ->
            PasswordUiState(password = password, retryPassword = retryPassword)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PasswordUiState()
        )

    val agreementUiState: StateFlow<AgreementUiState> =
        combine(_agreements) { agreements ->
            AgreementUiState(agreements.first())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AgreementUiState()
        )

    val profileImageUiState: StateFlow<ProfileImageUiState> =
        combine(_profileImage, _nickName) { profileImage, nickName ->
            ProfileImageUiState(profileImage = profileImage, nickName = nickName)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileImageUiState()
        )

    val categoryUiState: StateFlow<CategoryUiState> =
        combine(_nickName, _categoryTags) { nickName, categoryTags ->
            CategoryUiState(nickName = nickName, categoryTags = categoryTags)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CategoryUiState()
        )

    fun updateCategoryTags(categoryTag: CategoryTag) {
        _categoryTags.value = _categoryTags.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
    }

    fun updateProfileImage(profileImage: Uri?) {
        _profileImage.value = profileImage
    }

    fun updateNickName(nickname: String) {
        _nickName.value = _nickName.value.copy(text = nickname)
    }

    fun updateAgreements(agreement: MutableList<Agreement>) {
        _agreements.value = agreement
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