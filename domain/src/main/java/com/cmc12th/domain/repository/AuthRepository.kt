package com.cmc12th.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.DefaultApiWrapper
import com.cmc12th.domain.PagingApiWrapper
import com.cmc12th.domain.model.request.OauthLoginRequest
import com.cmc12th.domain.model.response.LoginResponse
import com.cmc12th.domain.model.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.domain.model.response.store.UserReviewDetail
import com.cmc12th.domain.model.response.user.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthRepository {

    fun getToken(type: Preferences.Key<String>): Flow<String>
    suspend fun setToken(type: Preferences.Key<String>, value: String)
    fun validateRefreshToken(refreshToken: String): Flow<ApiWrapper<LoginResponse>>
    fun logout(): Flow<DefaultApiWrapper>
    fun withdrawal(): Flow<DefaultApiWrapper>
    fun getMyInfo(): Flow<ApiWrapper<MyPageInfo>>
    fun getBookmarkedReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    fun getMyBookmarkedReviewDetail(
        reviewId: Int,
    ): Flow<ApiWrapper<UserReviewDetail>>

    fun getMyReviewDetail(
        reviewId: Int,
    ): Flow<ApiWrapper<UserReviewDetail>>

    fun getInformationManagementInfo(): Flow<ApiWrapper<UserInformationManagamentInfo>>
    fun linkToKakao(oauthLoginRequest: OauthLoginRequest): Flow<DefaultApiWrapper>
    fun unLinkToKakao(): Flow<DefaultApiWrapper>
    fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>>
    fun getMyReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    fun getBookmarkedStore(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<StoreMetaDataItem>>

    fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ): Flow<ApiWrapper<ImgUrlAndNicknameAndCategorys>>
}

