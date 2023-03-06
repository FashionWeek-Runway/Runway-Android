package com.cmc12th.runway.domain.repository

import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingData
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.data.response.user.*
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface AuthRepository {

    fun getToken(type: Preferences.Key<String>): Flow<String>
    suspend fun setToken(type: Preferences.Key<String>, value: String)
    fun validateRefreshToken(refreshToken: String): Flow<ApiWrapper<LoginResponse>>
    suspend fun logout(): Flow<DefaultApiWrapper>

    suspend fun getMyInfo(): Flow<ApiWrapper<MyPageInfo>>
    suspend fun getBookmarkedReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    suspend fun getMyBookmarkedReviewDetail(
        reviewId: Int,
    ): Flow<ApiWrapper<UserReviewDetail>>

    suspend fun getMyReviewDetail(
        reviewId: Int,
    ): Flow<ApiWrapper<UserReviewDetail>>

    suspend fun getInformationManagementInfo(): Flow<ApiWrapper<UserInformationManagamentInfo>>
    suspend fun linkToKakao(oauthLoginRequest: OauthLoginRequest): Flow<DefaultApiWrapper>
    suspend fun unLinkToKakao(): Flow<DefaultApiWrapper>
    suspend fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>>
    suspend fun getMyReview(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    suspend fun getBookmarkedStore(
        page: Int, size: Int,
    ): Flow<PagingApiWrapper<StoreMetaDataItem>>

    fun myReviewPaging(): Flow<PagingData<MyReviewsItem>>
    fun bookmarkedStorePaging(): Flow<PagingData<StoreMetaDataItem>>

    suspend fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ): Flow<ApiWrapper<ImgUrlAndNicknameAndCategorys>>
}

