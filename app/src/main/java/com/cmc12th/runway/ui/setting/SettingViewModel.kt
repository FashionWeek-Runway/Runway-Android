package com.cmc12th.runway.ui.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.request.auth.PasswordRequest
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.data.response.user.UserInformationManagamentInfo
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.ui.signin.model.Password
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingPersonalInfoUiState(
    val personalInfo: UserInformationManagamentInfo = UserInformationManagamentInfo.default(),
    val nickname: String = "",
)

data class EditPasswordUiState(
    val verifyPassword: Password = Password.default(),
    val newPassword: Password = Password.default(),
    val checkNewPassword: Password = Password.default(),
) {
    fun checkValidate() = newPassword.isValidatePassword(checkNewPassword)
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signInRepository: SignInRepository,
) : ViewModel() {

    private val _personalInfo = MutableStateFlow(UserInformationManagamentInfo.default())
    private val _nickname = MutableStateFlow("")

    private val _verifyPassword = MutableStateFlow(Password.default())
    private val _newPassword = MutableStateFlow(Password.default())
    private val _checkNewPassword = MutableStateFlow(Password.default())

    val personalInfoUiState: StateFlow<SettingPersonalInfoUiState> =
        combine(_personalInfo, _nickname) { personalInfo, nickname ->
            SettingPersonalInfoUiState(
                personalInfo = personalInfo,
                nickname = nickname
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingPersonalInfoUiState()
        )

    val editPasswordUiState: StateFlow<EditPasswordUiState> =
        combine(
            _verifyPassword,
            _newPassword,
            _checkNewPassword
        ) { verifyPassword, newPassword, checkNewPassword ->
            EditPasswordUiState(
                verifyPassword, newPassword, checkNewPassword
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EditPasswordUiState()
        )

    fun getPersonalInfo() = viewModelScope.launch {
        authRepository.getInformationManagementInfo().collectLatest { apiState ->
            apiState.onSuccess {
                _personalInfo.value = it.result
            }
        }
    }

    fun getNickname() = viewModelScope.launch {
        authRepository.getProfileInfoToEdit().collectLatest { apiState ->
            apiState.onSuccess {
                _nickname.value = it.result.nickname
            }
        }
    }

    fun logout(onSuccess: () -> Unit) = viewModelScope.launch {
        authRepository.setToken(AuthRepositoryImpl.ACCESS_TOKEN, "")
        authRepository.setToken(AuthRepositoryImpl.REFRESH_TOKEN, "")
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("dlgocks1", "?????? ?????? ??????", error)
            } else {
                Log.i("dlgocks1", "?????? ?????? ??????. SDK?????? ?????? ?????? ???")
            }
        }
        authRepository.logout().collect {
            it.onSuccess {
                onSuccess()
            }
        }
    }

    fun withdrawal(onSuccess: () -> Unit) = viewModelScope.launch {
        authRepository.setToken(AuthRepositoryImpl.ACCESS_TOKEN, "")
        authRepository.setToken(AuthRepositoryImpl.REFRESH_TOKEN, "")
        authRepository.withdrawal().collect {
            it.onSuccess {
                onSuccess()
            }
        }
    }

    private fun linkToKakao(
        kakaoToken: String,
        onSuccess: () -> Unit,
        onError: (ErrorResponse) -> Unit,
    ) = viewModelScope.launch {
        authRepository.linkToKakao(
            oauthLoginRequest = OauthLoginRequest(accessToken = kakaoToken)
        ).collect { apiState ->
            apiState.onSuccess {
                onSuccess()
            }
            apiState.onError(onError)
        }
    }

    fun getKakaoToken(
        onSuccess: () -> Unit,
        context: Context,
        onError: (ErrorResponse) -> Unit,
    ) {
        // ????????????????????? ????????? ?????? callback ??????
        // ?????????????????? ????????? ??? ??? ?????? ????????????????????? ???????????? ?????? ?????????
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("dlgocks1-KAKAO-LOGIN", "????????????????????? ????????? ??????", error)
            } else if (token != null) {
                Log.i("dlgocks1-KAKAO-LOGIN", "????????????????????? ????????? ?????? ${token.accessToken}")
                linkToKakao(token.accessToken, onSuccess, onError)
            }
        }
        // ??????????????? ???????????? ????????? ?????????????????? ?????????, ????????? ????????????????????? ?????????
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("dlgocks1-KAKAO-LOGIN", "?????????????????? ????????? ??????", error)
                    // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                    // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("dlgocks1-KAKAO-LOGIN", "?????????????????? ????????? ?????? ${token.accessToken}")
                    linkToKakao(token.accessToken, onSuccess, onError)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun updateKakaoLinkState(isLinked: Boolean) {
        _personalInfo.value = _personalInfo.value.copy(
            kakao = isLinked
        )
    }

    fun unLinkToKakao(onError: (ErrorResponse) -> Unit) = viewModelScope.launch {
        authRepository.unLinkToKakao().collect { apiState ->
            apiState.onSuccess {
                updateKakaoLinkState(false)
            }
            apiState.onError {
                onError(it)
            }
        }

    }

    fun verifyPassword(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) =
        viewModelScope.launch {
            signInRepository.verifyPassword(PasswordRequest(_verifyPassword.value.value))
                .collect { apiState ->
                    apiState.onSuccess {
                        onSuccess()
                    }
                    apiState.onError(onError)
                }
        }

    fun modifyPassword(onSuccess: () -> Unit) = viewModelScope.launch {
        signInRepository.modifyPassword(PasswordRequest(_checkNewPassword.value.value))
            .collect { apiState ->
                apiState.onSuccess {
                    onSuccess()
                }
            }

    }

    fun updateVerifyPassword(password: Password) {
        _verifyPassword.value = password
    }

    fun updateNewPassword(password: Password) {
        _newPassword.value = password
    }

    fun updateCheckNewPassword(password: Password) {
        _checkNewPassword.value = password
    }


}