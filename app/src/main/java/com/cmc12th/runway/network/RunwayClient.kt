package com.cmc12th.runway.network

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.network.service.AuthService
import com.cmc12th.runway.network.service.LoginService
import com.cmc12th.runway.network.service.MapService
import com.cmc12th.runway.network.service.StoreService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RunwayClient @Inject constructor(
    private val loginService: LoginService,
    private val authService: AuthService,
    private val storeService: StoreService,
    private val mapService: MapService,
) {

    /** Auth */
    suspend fun login(loginRequest: LoginRequest) =
        loginService.login(loginRequest)

    suspend fun sendVerifyMessage(sendVerifyMessageRequest: SendVerifyMessageRequest) =
        loginService.sendVerifyMessage(sendVerifyMessageRequest)

    suspend fun verifyPhoneNumber(loginCheckRequest: LoginCheckRequest) =
        loginService.verifyPhoneNumber(loginCheckRequest)

    suspend fun checkNickname(nickname: String) = loginService.checkNickname(nickname)

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
        multipartFile: MultipartBody.Part?,
    ) = loginService.kakoSignUp(feedPostReqeust, categoryList, multipartFile)

    suspend fun logout() = authService.logout()

    suspend fun loginRefresh(refreshToken: String) = authService.loginRefresh(refreshToken)


    /** 쇼룸 */
    suspend fun stores() = storeService.stores()

    suspend fun getBlogReview(storeId: Int, storeName: String) =
        storeService.getBlogReview(storeId, storeName)

    suspend fun getDetail(storeId: Int) = storeService.getDetail(storeId)

    suspend fun getUserReview(storeId: Int, page: Int, size: Int) =
        storeService.getUserReview(storeId, page, size)

    suspend fun writeUserReview(storeId: Int, img: MultipartBody.Part) =
        storeService.writeUserReview(storeId, img)


    /** 맵 */
    suspend fun mapsFiltering(mapFilterRequest: MapFilterRequest) =
        mapService.mapFiltering(mapFilterRequest)

    suspend fun mapsInfoPaging(page: Int, size: Int, mapFilterRequest: MapFilterRequest) =
        mapService.mapInfoPaging(page, size, mapFilterRequest)

    suspend fun mapSearch(mapSearchRequest: MapSearchRequest) =
        mapService.mapSearch(mapSearchRequest)

    suspend fun mapInfo(storeId: Int) =
        mapService.mapInfo(storeId)

    suspend fun storeSearch(storeId: Int) =
        mapService.mapSearchInfo(storeId)

}