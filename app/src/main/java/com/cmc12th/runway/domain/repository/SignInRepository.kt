package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.SignUpResponse
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SignInRepository {
    fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>>

    fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper>

    fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest): Flow<DefaultApiWrapper>

    fun checkNickname(nickname: String): Flow<DefaultApiWrapper>

    fun modifyPassword(passwordAndPhoneNumberRequest: PasswordAndPhoneNumberRequest): Flow<DefaultApiWrapper>

    fun signUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
        multipartFile: MultipartBody.Part?,
    ): Flow<ApiWrapper<SignUpResponse>>

    fun kakaoSignUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
        multipartFile: MultipartBody.Part?,
    ): Flow<ApiWrapper<SignUpResponse>>

    fun kakaoLogin(oauthLoginRequest: OauthLoginRequest): Flow<ApiWrapper<LoginResponse>>

}