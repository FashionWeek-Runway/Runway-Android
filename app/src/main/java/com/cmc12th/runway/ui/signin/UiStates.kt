package com.cmc12th.runway.ui.signin

import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.signin.SignInViewModel.Companion.DEFAULT_RETRY_TIME
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants.LOCATION_USE_TERMS
import com.cmc12th.runway.utils.Constants.MARKETING_INFO_TERMS
import com.cmc12th.runway.utils.Constants.PERSONAL_INFO_USE_TERMS
import com.cmc12th.runway.utils.Constants.SERVICE_TERMS

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
    val retryTime: Int = DEFAULT_RETRY_TIME,
)

data class SignInPasswordUiState(
    val password: Password = Password.default(),
    val retryPassword: Password = Password.default(),
) {
    fun checkValidate() = password.isValidatePassword(retryPassword)
}

data class SignInAgreementUiState(
    val agreements: MutableList<Agreement> = mutableListOf(
        Agreement(
            "이용약관 동의 (필수)", true,
            link = SERVICE_TERMS
        ),
        Agreement(
            "개인정보 처리 방침 동의 (필수)", true,
            link = PERSONAL_INFO_USE_TERMS
        ),
        Agreement(
            "위치정보 이용 약관 동의 (필수)", true,
            link = LOCATION_USE_TERMS
        ),
        Agreement(
            "마케팅 정보 수신 동의 (선택)", false,
            link = MARKETING_INFO_TERMS
        ),
    ),
) {
    fun isAllChcked() = agreements.filter { it.isRequire }.all { it.isChecked }
}

data class SignInProfileImageUiState(
    val profileImage: ProfileImageType = ProfileImageType.DEFAULT,
    val nickName: Nickname = Nickname.default(),
)


data class SignInCategoryUiState(
    val nickName: Nickname = Nickname.default(),
    val categoryTags: MutableList<CategoryTag> = RunwayCategory.generateCategoryTags(),
    val signInType: SignInType = SignInType.Phone,
) {
    fun anyCategorySelected() = categoryTags.any { it.isSelected }
}

data class SignInCompleteUiState(
    val nickName: Nickname = Nickname.default(),
    val profileImage: ProfileImageType = ProfileImageType.DEFAULT,
    val categoryTags: List<CategoryTag> = listOf(),
)
