package com.cmc12th.runway.ui.signin

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.ACCESS_TOKEN
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.REFRESH_TOKEN
import com.cmc12th.domain.model.request.LoginCheckRequest
import com.cmc12th.domain.model.request.SendVerifyMessageRequest
import com.cmc12th.domain.model.response.ErrorResponse
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.repository.SignInRepository
import com.cmc12th.runway.network.model.ServiceInterceptor
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.signin.model.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.fileFromContentUri
import com.cmc12th.runway.utils.toPlainRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInRepository: com.cmc12th.domain.repository.SignInRepository,
    private val authRepository: com.cmc12th.domain.repository.AuthRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _signInType: MutableStateFlow<com.cmc12th.domain.model.signin.model.SignInType> =
        MutableStateFlow(
            com.cmc12th.domain.model.signin.model.SignInType.Phone
        )
    private val _kakaoId = mutableStateOf("")
    val kakaoId: State<String> = _kakaoId

    private val _initialDialogVisiblity = mutableStateOf(true)
    val initialDialogVisiblity: State<Boolean> get() = _initialDialogVisiblity

    private val _nameAndNationality =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.NameAndNationality.default())
    private val _gender = MutableStateFlow(com.cmc12th.domain.model.signin.model.Gender.Unknown)
    private val _birth = MutableStateFlow(com.cmc12th.domain.model.signin.model.Birth.default())
    private val _phone = MutableStateFlow(com.cmc12th.domain.model.signin.model.Phone.default())
    private val _userVerificationStatus = MutableStateFlow(false)

    private val _retryTime = MutableStateFlow(DEFAULT_RETRY_TIME)
    private val _verifyCode = MutableStateFlow("")

    private val _password =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Password.default())
    private val _retryPassword =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Password.default())

    private val _agreements = MutableStateFlow(
        mutableListOf(
            com.cmc12th.domain.model.signin.model.Agreement(
                "이용약관 동의 (필수)", true,
                link = Constants.SERVICE_TERMS
            ),
            com.cmc12th.domain.model.signin.model.Agreement(
                "개인정보 처리 방침 동의 (필수)", true,
                link = Constants.PERSONAL_INFO_USE_TERMS
            ),
            com.cmc12th.domain.model.signin.model.Agreement(
                "위치정보 이용 약관 동의 (필수)", true,
                link = Constants.LOCATION_USE_TERMS
            ),
            com.cmc12th.domain.model.signin.model.Agreement(
                "마케팅 정보 수신 동의 (선택)", false,
                link = Constants.MARKETING_INFO_TERMS
            ),
        )
    )

    private val _nickName =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Nickname.default())
    private val _profileImage =
        MutableStateFlow<com.cmc12th.domain.model.signin.model.ProfileImageType>(
            com.cmc12th.domain.model.signin.model.ProfileImageType.DEFAULT
        )

    private val _categoryTags = MutableStateFlow(RunwayCategory.generateCategoryTags())


    private var timer = Timer()
    private var timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            _retryTime.update { _retryTime.value - 1 }
            if (_retryTime.value <= 0) {
                timer.cancel()
            }
        }
    }


    fun startTimer() {
        _retryTime.value = SignInViewModel.DEFAULT_RETRY_TIME
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                _retryTime.update { _retryTime.value - 1 }
                if (_retryTime.value <= 0) {
                    timer.cancel()
                }
            }
        }
        timer.schedule(timerTask, 0, 1000);
    }

    fun resetTimer() {
        _retryTime.value = SignInViewModel.DEFAULT_RETRY_TIME
        startTimer()
    }


    fun sendVerifyMessage(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) =
        viewModelScope.launch {
            val params =
                com.cmc12th.domain.model.request.SendVerifyMessageRequest(_phone.value.number)
            signInRepository.sendVerifyMessage(params).collect { apiState ->
                apiState.onSuccess { onSuccess() }
                apiState.onError { onError(it) }
            }
        }

    fun verifyPhoneNumber(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) =
        viewModelScope.launch {
            val params = com.cmc12th.domain.model.request.LoginCheckRequest(
                _verifyCode.value,
                _phone.value.number
            )
            signInRepository.verifyPhoneNumber(params).collect { apiState ->
                apiState.onSuccess { onSuccess() }
                apiState.onError { onError(it) }
            }
        }

    fun checkNickname(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) =
        viewModelScope.launch {
            signInRepository.checkNickname(nickname = _nickName.value.text).collect {
                it.onSuccess {
                    onSuccess()
                }
                it.onError(onError)
            }
        }

    fun signUp(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) = viewModelScope.launch {

        /** List형태 MultiPart 설정 */
        val categoryList: ArrayList<MultipartBody.Part> = ArrayList()
        categoryToMultipartBody(categoryList)

        /** 단일 인자 MultiPart 설정 */
        val feedPostReqeust = hashMapOf<String, RequestBody>()
        feedPostReqeust["gender"] = _gender.value.text.toPlainRequestBody()
        feedPostReqeust["name"] = _nameAndNationality.value.name.toPlainRequestBody()
        feedPostReqeust["nickname"] = _nickName.value.text.toPlainRequestBody()
        feedPostReqeust["password"] = _password.value.value.toPlainRequestBody()
        feedPostReqeust["phone"] = _phone.value.number.toPlainRequestBody()

        /** 이미지 파일 변환 */
        val multipartFile = convetProfileImageToMultipartFile()

        signInRepository.signUp(
            multipartFile = multipartFile,
            feedPostReqeust = feedPostReqeust,
            categoryList = categoryList
        ).collect { apiWrapper ->
            apiWrapper.onSuccess {
                onSignInComplete(it.result.accessToken, it.result.refreshToken)
                onSuccess()
            }
            apiWrapper.onError(onError)
        }
    }

    fun kakaoSignUp(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit,
    ) = viewModelScope.launch {
        /** List형태 MultiPart 설정 */
        val categoryList: ArrayList<MultipartBody.Part> = ArrayList()
        categoryToMultipartBody(categoryList)

        /** 단일 인자 MultiPart 설정 */
        val feedPostReqeust = hashMapOf<String, RequestBody>()
        feedPostReqeust["nickname"] = _nickName.value.text.toPlainRequestBody()
        feedPostReqeust["socialId"] = _kakaoId.value.toPlainRequestBody()
        feedPostReqeust["type"] = "KAKAO".toPlainRequestBody()
        feedPostReqeust["profileImgUrl"] = when (val profileImage = _profileImage.value) {
            is com.cmc12th.domain.model.signin.model.ProfileImageType.SOCIAL -> profileImage.imgUrl
            else -> ""
        }.toPlainRequestBody()

        /** 이미지 파일 변환 */
        val multipartFile = convetProfileImageToMultipartFile()

        signInRepository.kakaoSignUp(
            multipartFile = multipartFile,
            feedPostReqeust = feedPostReqeust,
            categoryList = categoryList
        ).collect { apiWrapper ->
            apiWrapper.onSuccess {
                onSignInComplete(it.result.accessToken, it.result.refreshToken)
                onSuccess()
            }
            apiWrapper.onError(onError)
        }
    }

    private fun onSignInComplete(accessToken: String, refreshToken: String) =
        viewModelScope.launch {
            ServiceInterceptor.accessToken = accessToken
            ServiceInterceptor.refreshToken = refreshToken
            authRepository.setToken(ACCESS_TOKEN, accessToken)
            authRepository.setToken(REFRESH_TOKEN, refreshToken)
        }

    private fun categoryToMultipartBody(categoryList: ArrayList<MultipartBody.Part>) {
        _categoryTags.value.filter { it.isSelected }.forEach {
            categoryList.add(
                MultipartBody.Part.createFormData(
                    "categoryList",
                    it.id.toString()
                )
            )
        }
    }

    private fun convetProfileImageToMultipartFile() =
        when (val profileImage = _profileImage.value) {
            com.cmc12th.domain.model.signin.model.ProfileImageType.DEFAULT -> null
            is com.cmc12th.domain.model.signin.model.ProfileImageType.SOCIAL -> null
            is com.cmc12th.domain.model.signin.model.ProfileImageType.LOCAL -> {
                val file = fileFromContentUri(context, profileImage.uri)
                val requestBody: RequestBody = file.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
            }
        }


    fun updateSignIntypeSocial(profileImage: String, kakaoId: String) {
        _signInType.value = com.cmc12th.domain.model.signin.model.SignInType.SOCIAL
        _profileImage.value =
            com.cmc12th.domain.model.signin.model.ProfileImageType.SOCIAL(profileImage)
        _kakaoId.value = kakaoId
    }

    fun updateCategoryTags(categoryTag: com.cmc12th.domain.model.signin.model.CategoryTag) {
        _categoryTags.value = _categoryTags.value.mapIndexed { _, item ->
            if (item.name == categoryTag.name) item.copy(isSelected = !item.isSelected) else item
        }.toMutableList()
    }

    fun updateProfileImage(profileImage: com.cmc12th.domain.model.signin.model.ProfileImageType) {
        _profileImage.value = profileImage
    }

    fun updateNickName(nickname: String) {
        _nickName.value = _nickName.value.copy(text = nickname)
    }

    fun updateAgreements(agreement: MutableList<com.cmc12th.domain.model.signin.model.Agreement>) {
        _agreements.value = agreement
    }

    fun updateVerifyCode(verifyCode: String) {
        _verifyCode.value = verifyCode
    }

    fun updatePassword(password: com.cmc12th.domain.model.signin.model.Password) {
        _password.value = password
    }

    fun updateRetryPassword(password: com.cmc12th.domain.model.signin.model.Password) {
        _retryPassword.value = password
    }

    fun updateDialogState(isvisible: Boolean) {
        _initialDialogVisiblity.value = isvisible
    }

    fun updateName(name: String) {
        _nameAndNationality.value = _nameAndNationality.value.copy(name = name)
        checkUserVerificationStatus()
    }

    fun updateNationality(nationality: com.cmc12th.domain.model.signin.model.Nationality) {
        _nameAndNationality.value = _nameAndNationality.value.copy(nationality = nationality)
    }

    fun updateGender(gender: com.cmc12th.domain.model.signin.model.Gender) {
        _gender.value = gender
        checkUserVerificationStatus()
    }

    fun updateBirth(birth: com.cmc12th.domain.model.signin.model.Birth) {
        _birth.value = birth
        checkUserVerificationStatus()
    }

    fun updateMobileCarrier(mobileCarrier: com.cmc12th.domain.model.signin.model.MobileCarrier) {
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

    val userVerificationUiState: StateFlow<SignInUserVerificationUiState> = combine(
        _nameAndNationality, _gender, _birth, _phone, _userVerificationStatus
    ) { nameAndNationality, gender, birth, phone, userVerificationStatus ->
        SignInUserVerificationUiState(
            nameAndNationality = nameAndNationality,
            gender = gender,
            birth = birth,
            phone = phone,
            userVerificationStatus = userVerificationStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUserVerificationUiState()
    )

    val phoneVerifyUiState: StateFlow<SignInPhoneVerifyUiState> =
        combine(_phone, _verifyCode, _retryTime) { phone, verifyCode, retryTime ->
            SignInPhoneVerifyUiState(phone = phone, verifyCode = verifyCode, retryTime = retryTime)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInPhoneVerifyUiState()
        )

    val passwordUiState: StateFlow<SignInPasswordUiState> =
        combine(_password, _retryPassword) { password, retryPassword ->
            SignInPasswordUiState(password = password, retryPassword = retryPassword)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInPasswordUiState()
        )

    val agreementUiState: StateFlow<SignInAgreementUiState> =
        combine(_agreements) { agreements ->
            SignInAgreementUiState(agreements.first())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInAgreementUiState()
        )

    val profileImageUiState: StateFlow<SignInProfileImageUiState> =
        combine(_profileImage, _nickName) { profileImage, nickName ->
            SignInProfileImageUiState(profileImage = profileImage, nickName = nickName)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInProfileImageUiState()
        )

    val categoryUiState: StateFlow<SignInCategoryUiState> =
        combine(_nickName, _categoryTags, _signInType) { nickName, categoryTags, signInType ->
            SignInCategoryUiState(
                nickName = nickName,
                categoryTags = categoryTags,
                signInType = signInType
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInCategoryUiState()
        )

    val complteUiState: StateFlow<SignInCompleteUiState> =
        combine(_nickName, _categoryTags, _profileImage) { nickName, categoryTags, profileImage ->
            SignInCompleteUiState(
                nickName = nickName,
                categoryTags = categoryTags.filter { it.isSelected },
                profileImage = profileImage
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInCompleteUiState()
        )

    companion object {
        const val DEFAULT_RETRY_TIME = 180
    }
}