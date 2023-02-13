package com.cmc12th.runway.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.repository.AuthRepositoryImpl.PreferenceKeys.ACCESS_TOKEN
import com.cmc12th.runway.di.DispatcherModule
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.network.model.ServiceInterceptor.Companion.authToken
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
            val token = withContext(IODispatcher) {
                authRepository.getToken(ACCESS_TOKEN).first()
            }
            authToken = token
            // 토큰이 빈 값이 아니라면 (로그인이 되어 있다면)
            if (token.isNotBlank()) {
                launch(MainDispatcher) { navigateToMain() }
            } else {
                launch(MainDispatcher) { navigateToLogin() }
            }
        }

}