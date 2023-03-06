package com.cmc12th.runway.ui.login.passwordsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.request.LoginCheckRequest
import com.cmc12th.runway.data.request.PasswordAndPhoneNumberRequest
import com.cmc12th.runway.data.request.SendVerifyMessageRequest
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.ui.signin.SignInPasswordUiState
import com.cmc12th.runway.ui.signin.SignInPhoneVerifyUiState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.MobileCarrier
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.signin.model.Phone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


data class SearchPasswordPhoneUiState(
    val phone: Phone = Phone.default(),
    val userPhoneVerificationStatus: Boolean = false,
)

@HiltViewModel
class PasswordSearchViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
) : ViewModel() {


    private val _phone: MutableStateFlow<Phone> = MutableStateFlow(Phone.default())
    private val _userPhoneVerificationStatus = MutableStateFlow(false)

    private val _retryTime = MutableStateFlow(SignInViewModel.DEFAULT_RETRY_TIME)
    private val _verifyCode = MutableStateFlow("")

    private val _password = MutableStateFlow(Password.default())
    private val _retryPassword = MutableStateFlow(Password.default())

    val searchPasswordPhoneUiState =
        combine(_phone, _userPhoneVerificationStatus) { phone, userPhoneVerificationStatus ->
            SearchPasswordPhoneUiState(
                phone = phone,
                userPhoneVerificationStatus = userPhoneVerificationStatus
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchPasswordPhoneUiState()
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

    private var timer = Timer()
    private var timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            _retryTime.update { _retryTime.value - 1 }
            if (_retryTime.value <= 0) {
                timer.cancel()
            }
        }
    }

    private fun checkUserVerificationStatus() {
        val isVerified = _phone.value.checkValidation()
        _userPhoneVerificationStatus.value = isVerified
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

    fun modifyPassword(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) =
        viewModelScope.launch {
            signInRepository.modifyPassword(
                passwordAndPhoneNumberRequest = PasswordAndPhoneNumberRequest(
                    phone = _phone.value.number,
                    password = _retryPassword.value.value
                )
            ).collect { apiState ->
                apiState.onSuccess { onSuccess() }
                apiState.onError { onError(it) }
            }
        }

    fun sendVerifyMessage(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) =
        viewModelScope.launch {
            val params = SendVerifyMessageRequest(_phone.value.number)
            signInRepository.sendVerifyMessage(params).collect { apiState ->
                apiState.onSuccess { onSuccess() }
                apiState.onError { onError(it) }
            }
        }

    fun verifyPhoneNumber(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) =
        viewModelScope.launch {
            val params = LoginCheckRequest(_verifyCode.value, _phone.value.number)
            signInRepository.verifyPhoneNumber(params).collect { apiState ->
                apiState.onSuccess { onSuccess() }
                apiState.onError { onError(it) }
            }
        }

    fun updateMobileCarrier(mobileCarrier: MobileCarrier) {
        _phone.value = _phone.value.copy(mobileCarrier = mobileCarrier)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _phone.value = _phone.value.copy(number = phoneNumber)
        checkUserVerificationStatus()
    }

    fun updateVerifyCode(verifyCode: String) {
        _verifyCode.value = verifyCode
    }

    fun updatePassword(password: Password) {
        _password.value = password
    }

    fun updateRetryPassword(password: Password) {
        _retryPassword.value = password
    }


}