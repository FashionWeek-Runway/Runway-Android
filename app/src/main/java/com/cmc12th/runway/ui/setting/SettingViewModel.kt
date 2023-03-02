package com.cmc12th.runway.ui.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.data.response.user.UserInformationManagamentInfo
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.SignInRepository
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
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signInRepository: SignInRepository,
) : ViewModel() {

    private val _personalInfo = MutableStateFlow(UserInformationManagamentInfo.default())

    val personalInfoUiState: StateFlow<SettingPersonalInfoUiState> =
        combine(_personalInfo) { flowArr ->
            SettingPersonalInfoUiState(personalInfo = flowArr[0] as UserInformationManagamentInfo)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingPersonalInfoUiState()
        )

    fun getPersonalInfo() = viewModelScope.launch {
        authRepository.getInformationManagementInfo().collectLatest { apiState ->
            apiState.onSuccess {
                _personalInfo.value = it.result
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

//    private fun kakaoLogin(
//        accessToken: String,
//        alreadyRegistered: () -> Unit,
//        notRegistered: (profileImageUrl: String, kakaoId: String) -> Unit,
//        onError: (ErrorResponse) -> Unit,
//    ) = viewModelScope.launch {
//        signInRepository.kakaoLogin(OauthLoginRequest(accessToken = accessToken))
//            .collect { apiWrapper ->
//                apiWrapper.onSuccess {
//                    onSignInComplete(it.result.accessToken, it.result.refreshToken)
//                    alreadyRegistered()
//                }
//                apiWrapper.onError { errorResponse ->
//                    // 등록이 안되있는 유저일 때
//                    if (errorResponse.code == "U022") {
//                        // errorBody로 오는 String은 따로 파싱하여 사용해야 할 듯 도저히 구현이 안됨
//                        /** Json을 refied를 사용하여 Generic Obejct로 파싱 */
//                        val notRegisteredResponse: ResponseWrapper<NotRegisteredResponse> =
//                            GsonHelper.fromJson(errorResponse.totalResponse)
//                        notRegistered(
//                            notRegisteredResponse.result.profileImgUrl,
//                            notRegisteredResponse.result.kakaoId
//                        )
//                    } else {
//                        onError(errorResponse)
//                    }
//                }
//            }
//    }
}