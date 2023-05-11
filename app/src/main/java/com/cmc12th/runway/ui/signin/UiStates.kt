package com.cmc12th.runway.ui.signin

import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.signin.SignInViewModel.Companion.DEFAULT_RETRY_TIME
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants.LOCATION_USE_TERMS
import com.cmc12th.runway.utils.Constants.MARKETING_INFO_TERMS
import com.cmc12th.runway.utils.Constants.PERSONAL_INFO_USE_TERMS
import com.cmc12th.runway.utils.Constants.SERVICE_TERMS

data class SignInUserVerificationUiState(
    val nameAndNationality: com.cmc12th.domain.model.signin.model.NameAndNationality = com.cmc12th.domain.model.signin.model.NameAndNationality.default(),
    val gender: com.cmc12th.domain.model.signin.model.Gender = com.cmc12th.domain.model.signin.model.Gender.Unknown,
    val birth: com.cmc12th.domain.model.signin.model.Birth = com.cmc12th.domain.model.signin.model.Birth.default(),
    val phone: com.cmc12th.domain.model.signin.model.Phone = com.cmc12th.domain.model.signin.model.Phone.default(),
    val userVerificationStatus: Boolean = false,
)

data class SignInPhoneVerifyUiState(
    val phone: com.cmc12th.domain.model.signin.model.Phone = com.cmc12th.domain.model.signin.model.Phone.default(),
    val verifyCode: String = "",
    val retryTime: Int = DEFAULT_RETRY_TIME,
)

data class SignInPasswordUiState(
    val password: com.cmc12th.domain.model.signin.model.Password = com.cmc12th.domain.model.signin.model.Password.default(),
    val retryPassword: com.cmc12th.domain.model.signin.model.Password = com.cmc12th.domain.model.signin.model.Password.default(),
) {
    fun checkValidate() = password.isValidatePassword(retryPassword)
}

data class SignInAgreementUiState(
    val agreements: MutableList<com.cmc12th.domain.model.signin.model.Agreement> = mutableListOf(
        com.cmc12th.domain.model.signin.model.Agreement(
            "이용약관 동의 (필수)", true,
            link = SERVICE_TERMS
        ),
        com.cmc12th.domain.model.signin.model.Agreement(
            "개인정보 처리 방침 동의 (필수)", true,
            link = PERSONAL_INFO_USE_TERMS
        ),
        com.cmc12th.domain.model.signin.model.Agreement(
            "위치정보 이용 약관 동의 (필수)", true,
            link = LOCATION_USE_TERMS
        ),
        com.cmc12th.domain.model.signin.model.Agreement(
            "마케팅 정보 수신 동의 (선택)", false,
            link = MARKETING_INFO_TERMS
        ),
    ),
) {
    fun isAllChcked() = agreements.filter { it.isRequire }.all { it.isChecked }
}

data class SignInProfileImageUiState(
    val profileImage: com.cmc12th.domain.model.signin.model.ProfileImageType = com.cmc12th.domain.model.signin.model.ProfileImageType.DEFAULT,
    val nickName: com.cmc12th.domain.model.signin.model.Nickname = com.cmc12th.domain.model.signin.model.Nickname.default(),
)


data class SignInCategoryUiState(
    val nickName: com.cmc12th.domain.model.signin.model.Nickname = com.cmc12th.domain.model.signin.model.Nickname.default(),
    val categoryTags: MutableList<com.cmc12th.domain.model.signin.model.CategoryTag> = RunwayCategory.generateCategoryTags(),
    val signInType: com.cmc12th.domain.model.signin.model.SignInType = com.cmc12th.domain.model.signin.model.SignInType.Phone,
) {
    fun anyCategorySelected() = categoryTags.any { it.isSelected }
}

data class SignInCompleteUiState(
    val nickName: com.cmc12th.domain.model.signin.model.Nickname = com.cmc12th.domain.model.signin.model.Nickname.default(),
    val profileImage: com.cmc12th.domain.model.signin.model.ProfileImageType = com.cmc12th.domain.model.signin.model.ProfileImageType.DEFAULT,
    val categoryTags: List<com.cmc12th.domain.model.signin.model.CategoryTag> = listOf(),
)
