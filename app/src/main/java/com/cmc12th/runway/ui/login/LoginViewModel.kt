package com.cmc12th.runway.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.cmc12th.runway.ui.signin.model.Password
import com.cmc12th.runway.ui.signin.model.Phone
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
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

}