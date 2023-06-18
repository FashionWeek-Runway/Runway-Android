package com.cmc12th.runway.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.domain.GsonHelper
import com.cmc12th.domain.model.ServiceInterceptor
import com.cmc12th.domain.model.signin.*
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.repository.SignInRepository
import com.cmc12th.runway.data.repository.AuthRepositoryImpl
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginIdPasswordUiState(
    val phoneNumber: Phone = Phone.default(),
    val password: Password = Password.default(),
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _phoneNumber =
        MutableStateFlow(Phone.default())
    private val _password =
        MutableStateFlow(Password.default())

    val loginIdPasswordUiState = combine(_phoneNumber, _password) { phoneNumber, password ->
        LoginIdPasswordUiState(phoneNumber, password)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginIdPasswordUiState()
    )

    fun updatePhoneNumber(number: String) {
        _phoneNumber.value = _phoneNumber.value.copy(number = number)
    }

    fun updatePassword(password: String) {
        _password.value = _password.value.copy(value = password)
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit
    ) = viewModelScope.launch {
        signInRepository.login(
            com.cmc12th.domain.model.request.LoginRequest(
                password = _password.value.value,
                phone = _phoneNumber.value.number
            )
        ).collectLatest { apiWrapper ->
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
            authRepository.setToken(AuthRepositoryImpl.ACCESS_TOKEN, accessToken)
            authRepository.setToken(AuthRepositoryImpl.REFRESH_TOKEN, refreshToken)
        }

    fun getKakaoToken(
        alreadyRegistered: () -> Unit,
        notRegistered: (profileImageUrl: String, kakaoId: String) -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit,
        context: Context,
    ) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                kakaoLogin(token.accessToken, alreadyRegistered, notRegistered, onError)
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    kakaoLogin(token.accessToken, alreadyRegistered, notRegistered, onError)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun kakaoLogin(
        accessToken: String,
        alreadyRegistered: () -> Unit,
        notRegistered: (profileImageUrl: String, kakaoId: String) -> Unit,
        onError: (com.cmc12th.domain.model.response.ErrorResponse) -> Unit,
    ) = viewModelScope.launch {
        signInRepository.kakaoLogin(com.cmc12th.domain.model.request.OauthLoginRequest(accessToken = accessToken))
            .collect { apiWrapper ->
                apiWrapper.onSuccess {
                    onSignInComplete(it.result.accessToken, it.result.refreshToken)
                    alreadyRegistered()
                }
                apiWrapper.onError { errorResponse ->
                    // 등록이 안되있는 유저일 때
                    if (errorResponse.code == "U022") {
                        // errorBody로 오는 String은 따로 파싱하여 사용해야 할 듯 도저히 구현이 안됨
                        /** Json을 refied를 사용하여 Generic Obejct로 파싱 */
                        val notRegisteredResponse: com.cmc12th.domain.model.response.ResponseWrapper<com.cmc12th.domain.model.response.NotRegisteredResponse> =
                            GsonHelper.fromJson(errorResponse.totalResponse)
                        notRegistered(
                            notRegisteredResponse.result.profileImgUrl,
                            notRegisteredResponse.result.kakaoId
                        )
                    } else {
                        onError(errorResponse)
                    }
                }
            }
    }


    companion object {
        private const val TAG = "LOGIN_VIEWMODEL"
    }
}