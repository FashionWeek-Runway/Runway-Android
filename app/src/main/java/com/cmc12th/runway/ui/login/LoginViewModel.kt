package com.cmc12th.runway.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.signin.model.Phone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInRepository: SignInRepository
) : ViewModel() {
    private val _phoneNumber = mutableStateOf(Phone.default())
    val phoneNumber: State<Phone> get() = _phoneNumber

    private val _password = mutableStateOf(Password.default())
    val password: State<Password> get() = _password

    fun updatePhoneNumber(number: String) {
        _phoneNumber.value = _phoneNumber.value.copy(number = number)
    }

    fun updatePassword(password: String) {
        _password.value = _password.value.copy(value = password)
    }

    fun login() = viewModelScope.launch {
        signInRepository.login(LoginRequest(password = "1234", phone = "01026972321"))
            .collectLatest {
                it.onSuccess {

                }
                it.onLoading {

                }
                it.onNotResponse {

                }
            }
    }
}