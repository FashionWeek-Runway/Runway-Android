package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.SignUpResponse
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.safeFlow
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : SignInRepository {

    override fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>> =
        safeFlow {
            runwayClient.login(loginRequest)
        }

    override fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.sendVerifyMessage(sendVerifyMessageRequest)
        }

    override fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.verifyPhoneNumber(loginCheckRequest)
        }

    override fun checkNickname(nickname: String): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.checkNickname(nickname)
    }

    override fun modifyPassword(passwordAndPhoneNumberRequest: PasswordAndPhoneNumberRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.modifyPassword(passwordAndPhoneNumberRequest)
        }

    override fun signUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
        multipartFile: MultipartBody.Part?,
    ): Flow<ApiWrapper<SignUpResponse>> = safeFlow {
        runwayClient.signUp(feedPostReqeust, categoryList, multipartFile)
    }

    override fun kakaoSignUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
        multipartFile: MultipartBody.Part?,
    ): Flow<ApiWrapper<SignUpResponse>> = safeFlow {
        runwayClient.kakoSignUp(feedPostReqeust, categoryList, multipartFile)
    }

    override fun kakaoLogin(oauthLoginRequest: OauthLoginRequest): Flow<ApiWrapper<LoginResponse>> =
        safeFlow {
            runwayClient.kakaoLogin(oauthLoginRequest)
        }
}