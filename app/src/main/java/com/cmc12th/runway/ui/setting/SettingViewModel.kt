package com.cmc12th.runway.ui.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl
import com.cmc12th.domain.model.request.OauthLoginRequest
import com.cmc12th.domain.model.request.auth.PasswordRequest
import com.cmc12th.domain.model.response.ErrorResponse
import com.cmc12th.runway.data.response.user.UserInformationManagamentInfo
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.repository.SignInRepository
import com.cmc12th.domain.model.signin.model.Password
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
    val verifyPassword: com.cmc12th.domain.model.signin.model.Password = com.cmc12th.domain.model.signin.model.Password.default(),
    val newPassword: com.cmc12th.domain.model.signin.model.Password = com.cmc12th.domain.model.signin.model.Password.default(),
    val checkNewPassword: com.cmc12th.domain.model.signin.model.Password = com.cmc12th.domain.model.signin.model.Password.default(),
) {
    fun checkValidate() = newPassword.isValidatePassword(checkNewPassword)
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: com.cmc12th.domain.repository.AuthRepository,
    private val signInRepository: com.cmc12th.domain.repository.SignInRepository,
) : ViewModel() {

    private val _personalInfo = MutableStateFlow(UserInformationManagamentInfo.default())
    private val _nickname = MutableStateFlow("")

    private val _verifyPassword =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Password.default())
    private val _newPassword =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Password.default())
    private val _checkNewPassword =
        MutableStateFlow(com.cmc12th.domain.model.signin.model.Password.default())

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
                Log.e("dlgocks1", "연결 끊기 실패", error)
            } else {
                Log.i("dlgocks1", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
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
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit,
    ) = viewModelScope.launch {
        authRepository.linkToKakao(
            oauthLoginRequest = com.cmc12th.domain.model.request.OauthLoginRequest(accessToken = kakaoToken)
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
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit,
    ) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("dlgocks1-KAKAO-LOGIN", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("dlgocks1-KAKAO-LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                linkToKakao(token.accessToken, onSuccess, onError)
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("dlgocks1-KAKAO-LOGIN", "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("dlgocks1-KAKAO-LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
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

    fun unLinkToKakao(onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit) =
        viewModelScope.launch {
            authRepository.unLinkToKakao().collect { apiState ->
                apiState.onSuccess {
                    updateKakaoLinkState(false)
                }
                apiState.onError {
                    onError(it)
                }
            }

        }

    fun verifyPassword(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) =
        viewModelScope.launch {
            signInRepository.verifyPassword(
                com.cmc12th.domain.model.request.auth.PasswordRequest(
                    _verifyPassword.value.value
                )
            )
                .collect { apiState ->
                    apiState.onSuccess {
                        onSuccess()
                    }
                    apiState.onError(onError)
                }
        }

    fun modifyPassword(onSuccess: () -> Unit) = viewModelScope.launch {
        signInRepository.modifyPassword(
            com.cmc12th.domain.model.request.auth.PasswordRequest(
                _checkNewPassword.value.value
            )
        )
            .collect { apiState ->
                apiState.onSuccess {
                    onSuccess()
                }
            }

    }

    fun updateVerifyPassword(password: com.cmc12th.domain.model.signin.model.Password) {
        _verifyPassword.value = password
    }

    fun updateNewPassword(password: com.cmc12th.domain.model.signin.model.Password) {
        _newPassword.value = password
    }

    fun updateCheckNewPassword(password: com.cmc12th.domain.model.signin.model.Password) {
        _checkNewPassword.value = password
    }


}