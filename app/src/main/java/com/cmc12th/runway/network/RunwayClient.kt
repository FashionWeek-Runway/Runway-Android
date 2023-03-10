package com.cmc12th.runway.network

import com.cmc12th.runway.data.request.*
import com.cmc12th.runway.data.request.auth.PasswordRequest
import com.cmc12th.runway.data.request.map.MapFilterRequest
import com.cmc12th.runway.data.request.map.MapSearchRequest
import com.cmc12th.runway.data.request.store.ReviewReportRequest
import com.cmc12th.runway.data.response.user.PatchCategoryBody
import com.cmc12th.runway.network.service.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import javax.inject.Inject

class RunwayClient @Inject constructor(
    private val loginService: LoginService,
    private val authService: AuthService,
    private val storeService: StoreService,
    private val mapService: MapService,
    private val homeService: HomeService,
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

    suspend fun verifyPassword(passwordRequest: PasswordRequest) =
        authService.verifyPassword(passwordRequest)

    suspend fun modifyPassword(passwordRequest: PasswordRequest) =
        authService.modifyPassword(passwordRequest)

    /** ??????????????? */
    suspend fun logout() = authService.logout()
    suspend fun withdrawal() = authService.withdrawal()
    suspend fun loginRefresh(refreshToken: String) = authService.loginRefresh(refreshToken)
    suspend fun getMyInfo() = authService.getMyInfo()
    suspend fun getBookmarkedReview(page: Int, size: Int) =
        authService.getBookmarkedReview(page, size)

    suspend fun getMyBookmarkedReviewDetail(reviewId: Int) =
        authService.getMyBookmarkedReviewDetail(reviewId)

    suspend fun getInformationManagementInfo() = authService.getInformationManagementInfo()
    suspend fun linkToKakao(oauthLoginRequest: OauthLoginRequest) =
        authService.linkToKakao(oauthLoginRequest)

    suspend fun unLinkToKakao() = authService.unLinkToKakao()
    suspend fun getProfileInfoToEdit() = authService.getProfileInfoToEdit()
    suspend fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ) = authService.patchProfileImage(basic, multipartFile, nickname)

    suspend fun getMyReview(page: Int, size: Int) = authService.getMyReview(page, size)
    suspend fun getBookmarkedStore(page: Int, size: Int) =
        authService.getBookmarkedStore(page, size)

    suspend fun getMyReviewDetail(reviewId: Int) = authService.getMyReviewDetail(reviewId)


    /** ?????? */
    suspend fun storeBookmark(storeId: Int) = storeService.storeBookmark(storeId)

    suspend fun getBlogReview(storeId: Int, storeName: String) =
        storeService.getBlogReview(storeId, storeName)

    suspend fun getDetail(storeId: Int) = storeService.getDetail(storeId)

    suspend fun getUserReview(storeId: Int, page: Int, size: Int) =
        storeService.getUserReview(storeId, page, size)

    suspend fun writeUserReview(storeId: Int, img: RequestBody) =
        storeService.writeUserReview(storeId, img)

    suspend fun getReviewDetail(reviewId: Int) = storeService.getReviewDetail(reviewId)

    suspend fun reviewBookmark(reviewId: Int) = storeService.reviewBookmark(reviewId)

    suspend fun delteReview(reviewId: Int) = storeService.delteReview(reviewId)

    suspend fun reportReview(reviewReportRequest: ReviewReportRequest) =
        storeService.reportReview(reviewReportRequest)

    /** ??? */
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

    suspend fun mapRegionMarkerInfo(regionId: Int) =
        mapService.mapRegionMarkerInfo(regionId)

    suspend fun mapRegionInfoPaging(
        regionId: Int,
        page: Int,
        size: Int,
    ) = mapService.mapRegionInfoPaging(regionId, page, size)

    /** ??? */
    suspend fun getHomeBanner(type: Int) = homeService.getHomeBanner(type)
    suspend fun getHomeReview(page: Int, size: Int) = homeService.getHomeReview(page, size)
    suspend fun getHomeReviewDetail(reviewId: Int) = homeService.getHomeReviewDetail(reviewId)
    suspend fun getCategorys() = authService.getCategories()
    suspend fun setCategorys(patchCategoryBody: PatchCategoryBody) =
        authService.setCategories(patchCategoryBody)

}