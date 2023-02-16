package com.cmc12th.runway.ui.splash

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.ACCESS_TOKEN
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.REFRESH_TOKEN
import com.cmc12th.runway.di.DispatcherModule
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.network.model.ServiceInterceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @DispatcherModule.IoDispatcher private val IODispatcher: CoroutineDispatcher,
    @DispatcherModule.MainDispatcher private val MainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun loginCheck(navigateToMain: () -> Unit, navigateToLogin: () -> Unit) =
        viewModelScope.launch {
            val accessToken = withContext(IODispatcher) {
                authRepository.getToken(ACCESS_TOKEN).first()
            }
            val refreshToken = withContext(IODispatcher) {
                authRepository.getToken(REFRESH_TOKEN).first()
            }
            if (accessToken.isNotBlank()) {
                // 엑세스토큰이 빈 값이 아니라면 (로그인이 되어 있다면) 프레시토큰 검증을한다.
                validateRefreshToken(navigateToLogin, navigateToMain, refreshToken)
            } else {
                launch(MainDispatcher) { navigateToLogin() }
            }
        }

    private fun validateRefreshToken(
        navigateToLogin: () -> Unit,
        navigateToMain: () -> Unit,
        refreshToken: String,
    ) = viewModelScope.launch {
        authRepository.validateRefreshToken(refreshToken).collect { apiState ->
            apiState.onSuccess {
                val newAccessToken = it.result.accessToken
                val newRrefreshToken = it.result.refreshToken
                Log.i("ACCESS-TOKEN", newAccessToken)
                Log.i("REFRESH-TOKEN", newRrefreshToken)
                setToken(ACCESS_TOKEN, newAccessToken)
                setToken(REFRESH_TOKEN, newRrefreshToken)
                ServiceInterceptor.accessToken = newAccessToken
                ServiceInterceptor.refreshToken = newRrefreshToken
                launch(MainDispatcher) { navigateToMain() }
            }
            apiState.onError {
                launch(MainDispatcher) { navigateToLogin() }
            }
        }
    }

    private fun setToken(type: Preferences.Key<String>, token: String) = viewModelScope.launch {
        authRepository.setToken(type, token)
    }

}