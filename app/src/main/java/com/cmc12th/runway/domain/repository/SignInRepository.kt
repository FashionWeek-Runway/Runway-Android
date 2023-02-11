package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.request.SendVerifyMessageRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.ApiState
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.DefaultResponse
import kotlinx.coroutines.flow.Flow

interface SignInRepository {
    fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>>

    fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper>

}