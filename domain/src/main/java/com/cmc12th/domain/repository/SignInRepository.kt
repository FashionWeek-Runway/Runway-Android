package com.cmc12th.domain.repository

import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.DefaultApiWrapper
import com.cmc12th.domain.model.request.*
import com.cmc12th.domain.model.request.auth.PasswordRequest
import com.cmc12th.domain.model.response.LoginResponse
import com.cmc12th.domain.model.response.SignUpResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SignInRepository {
    fun login(loginRequest: LoginRequest): Flow<ApiWrapper<LoginResponse>>

    fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest): Flow<DefaultApiWrapper>

    fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest): Flow<DefaultApiWrapper>

    fun checkNickname(nickname: String): Flow<DefaultApiWrapper>

    fun verifyPassword(passwordRequest: PasswordRequest): Flow<DefaultApiWrapper>

    fun modifyPassword(passwordRequest: PasswordRequest): Flow<DefaultApiWrapper>

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