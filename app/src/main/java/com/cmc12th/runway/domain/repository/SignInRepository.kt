package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.request.LoginCheckRequest
import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.request.PasswordAndPhoneNumberRequest
import com.cmc12th.runway.data.request.SendVerifyMessageRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.ApiState
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.DefaultResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Query

interface SignInRepository {
    fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>>

    fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper>

    fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest): Flow<DefaultApiWrapper>

    fun checkNickname(nickname: String): Flow<DefaultApiWrapper>

    fun modifyPassword(passwordAndPhoneNumberRequest: PasswordAndPhoneNumberRequest): Flow<DefaultApiWrapper>

}