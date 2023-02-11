package com.cmc12th.runway.ui.signin

import android.net.Uri
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants

data class SignInUserVerificationUiState(
    val nameAndNationality: NameAndNationality = NameAndNationality.default(),
    val gender: Gender = Gender.Unknown,
    val birth: Birth = Birth.default(),
    val phone: Phone = Phone.default(),
    val userVerificationStatus: Boolean = false,
)

data class SignInPhoneVerifyUiState(
    val phone: Phone = Phone.default(),
    val verifyCode: String = "",
)

data class SignInPasswordUiState(
    val password: Password = Password.default(),
    val retryPassword: Password = Password.default(),
) {
    fun checkValidate() = password.isValidatePassword(retryPassword)
}

data class SignInAgreementUiState(
    val agreements: MutableList<Agreement> = mutableListOf(
        Agreement("이용약관 동의 (필수)", true),
        Agreement("개인정보 처리 방침 동의 (필수)", true),
        Agreement("위치정보 이용 약관 동의 (필수)", true),
        Agreement("마케팅 정보 수신 동의 (선택)", false),
    ),
) {
    fun isAllChcked() = agreements.filter { it.isRequire }.all { it.isChecked }
}

data class SignInProfileImageUiState(
    val profileImage: Uri? = null,
    val nickName: Nickname = Nickname.default(),
)


data class SignInCategoryUiState(
    val nickName: Nickname = Nickname.default(),
    val categoryTags: MutableList<CategoryTag> = Constants.CATEGORYS.map {
        CategoryTag(it)
    }.toMutableList(),
) {
    fun anyCategorySelected() = categoryTags.any { it.isSelected }
}

data class SignInCompleteUiState(
    val nickName: Nickname = Nickname.default(),
    val profileImage: Uri? = null,
    val categoryTags: List<CategoryTag> = listOf<CategoryTag>()
)
