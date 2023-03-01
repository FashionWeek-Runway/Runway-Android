package com.cmc12th.runway.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.data.response.user.*
import com.cmc12th.runway.network.model.ApiState
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.DefaultResponse
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthRepository {

    fun getToken(type: Preferences.Key<String>): Flow<String>
    suspend fun setToken(type: Preferences.Key<String>, value: String)
    fun validateRefreshToken(refreshToken: String): Flow<ApiWrapper<LoginResponse>>
    suspend fun logout(): Flow<DefaultApiWrapper>

    suspend fun getMyInfo(): Flow<ApiWrapper<MyPageInfo>>
    suspend fun getBookmarkedReview(
        page: Int, size: Int
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    suspend fun getMyBookmarkedReviewDetail(
        reviewId: Int
    ): Flow<ApiWrapper<UserReviewDetail>>

    suspend fun getInformationManagementInfo(): Flow<ApiWrapper<UserInformationManagamentInfo>>
    suspend fun linkToKakao(oauthLoginRequest: OauthLoginRequest): Flow<DefaultApiWrapper>
    suspend fun unLinkToKakao(): Flow<DefaultApiWrapper>
    suspend fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>>
    suspend fun getMyReview(
        page: Int, size: Int
    ): Flow<PagingApiWrapper<MyReviewsItem>>

    suspend fun getBookmarkedStore(
        page: Int, size: Int
    ): Flow<PagingApiWrapper<StoreMetaDataItem>>

}

