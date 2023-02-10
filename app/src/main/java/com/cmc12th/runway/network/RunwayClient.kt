package com.cmc12th.runway.network

import com.cmc12th.runway.data.request.LoginRequest
import javax.inject.Inject

class RunwayClient @Inject constructor(
    private val loginService: LoginService,
) {
    /** Login */
    suspend fun login(loginRequest: LoginRequest) =
        loginService.login(loginRequest)

}