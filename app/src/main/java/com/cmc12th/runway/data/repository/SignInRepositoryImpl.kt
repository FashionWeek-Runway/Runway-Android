package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.network.ApiState
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.safeFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient
) : SignInRepository {

    override fun login(loginRequest: LoginRequest): Flow<ApiState<ResponseWrapper<LoginResponse?>>> =
        safeFlow {
            runwayClient.login(loginRequest)
        }
}