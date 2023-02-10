package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.ApiState
import kotlinx.coroutines.flow.Flow

interface SignInRepository {
    fun login(loginRequest: LoginRequest): Flow<ApiState<ResponseWrapper<LoginResponse?>>>
}