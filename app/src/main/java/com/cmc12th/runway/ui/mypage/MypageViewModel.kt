package com.cmc12th.runway.ui.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.ACCESS_TOKEN
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.REFRESH_TOKEN
import com.cmc12th.runway.domain.repository.AuthRepository
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun logout(onSuccess: () -> Unit) = viewModelScope.launch {
        authRepository.setToken(ACCESS_TOKEN, "")
        authRepository.setToken(REFRESH_TOKEN, "")
//        UserApiClient.instance.logout { error ->
//            if (error != null) {
//                Log.e("dlgocks1", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//            } else {
//                Log.i("dlgocks1", "로그아웃 성공. SDK에서 토큰 삭제됨")
//            }
//        }
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

}