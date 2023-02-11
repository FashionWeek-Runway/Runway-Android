package com.cmc12th.runway.network

import com.cmc12th.runway.data.request.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RunwayClient @Inject constructor(
    private val loginService: LoginService,
) {

    suspend fun login(loginRequest: LoginRequest) =
        loginService.login(loginRequest)

    suspend fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest) =
        loginService.sendVerifyMessage(sendVerifyMessageRequest)

    suspend fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest) =
        loginService.verifyPhoneNumber(loginCheckRequest)

    suspend fun checkNickname(nickname: String) = loginService.checkNickname(nickname)

    suspend fun duplicatedPhoneNumberCheck(phone: String) =
        loginService.duplicatedPhoneNumberCheck(phone)

    suspend fun kakaoLogin(oauthLoginRequest: OauthLoginRequest) =
        loginService.kakaoLogin(oauthLoginRequest)

    suspend fun modifyPassword(passwordAndPhoneNumberRequest: PasswordAndPhoneNumberRequest) =
        loginService.modifyPassword(passwordAndPhoneNumberRequest)

    suspend fun signUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
        multipartFile: MultipartBody.Part?,
    ) = loginService.signUp(feedPostReqeust, categoryList, multipartFile)

    suspend fun kakoSignUp(
        feedPostReqeust: HashMap<String, RequestBody>,
        categoryList: List<MultipartBody.Part>,
    ) = loginService.kakoSignUp(feedPostReqeust, categoryList)


}