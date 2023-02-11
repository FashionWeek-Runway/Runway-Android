package com.cmc12th.runway.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.ui.signin.SignInUserVerificationUiState
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.signin.model.Phone
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginIdPasswordUiState(
    val phoneNumber: Phone = Phone.default(),
    val password: Password = Password.default()
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {
    private val _phoneNumber = MutableStateFlow(Phone.default())
    private val _password = MutableStateFlow(Password.default())

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

    fun login(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) = viewModelScope.launch {
        signInRepository.login(
            LoginRequest(
                password = _password.value.value,
                phone = _phoneNumber.value.number
            )
        ).collectLatest {
            it.onSuccess {
                onSuccess()
            }
            it.onError(onError)
        }
    }
}