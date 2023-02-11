package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.request.LoginRequest
import com.cmc12th.runway.data.request.SendVerifyMessageRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.network.ApiState
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.safeFlow
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient
) : SignInRepository {

    override fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>> =
        safeFlow {
            runwayClient.login(loginRequest)
        }

    override fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.sendVerifyMessage(sendVerifyMessageRequest)
        }
}