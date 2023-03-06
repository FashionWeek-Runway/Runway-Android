package com.cmc12th.runway.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.runway.data.pagingsource.BookmarkedPagingSource
import com.cmc12th.runway.data.pagingsource.MyReviewPagingSource
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.LoginResponse
import com.cmc12th.runway.data.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.runway.data.response.store.UserReviewDetail
import com.cmc12th.runway.data.response.user.*
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.safeFlow
import com.cmc12th.runway.network.model.safePagingFlow
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
    private val runwayClient: RunwayClient,
) : AuthRepository {

    override suspend fun setToken(type: Preferences.Key<String>, value: String) {
        preferenceDataStore.edit { settings ->
            settings[type] = value
        }
    }

    override fun getToken(type: Preferences.Key<String>): Flow<String> {
        return preferenceDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { prefs ->
                prefs[type] ?: ""
            }
    }

    override fun validateRefreshToken(refreshToken: String): Flow<ApiWrapper<LoginResponse>> =
        safeFlow {
            runwayClient.loginRefresh(refreshToken)
        }

    override fun logout(): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.logout()
    }

    override fun getMyInfo(): Flow<ApiWrapper<MyPageInfo>> = safeFlow {
        runwayClient.getMyInfo()
    }

    override fun getBookmarkedReview(
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<MyReviewsItem>> = safePagingFlow {
        runwayClient.getBookmarkedReview(page, size)
    }

    override fun getMyBookmarkedReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>> =
        safeFlow {
            runwayClient.getMyBookmarkedReviewDetail(reviewId)
        }

    override fun getMyReviewDetail(reviewId: Int): Flow<ApiWrapper<UserReviewDetail>> =
        safeFlow {
            runwayClient.getMyReviewDetail(reviewId)
        }

    override fun getInformationManagementInfo(): Flow<ApiWrapper<UserInformationManagamentInfo>> =
        safeFlow {
            runwayClient.getInformationManagementInfo()
        }

    override fun linkToKakao(oauthLoginRequest: OauthLoginRequest): Flow<DefaultApiWrapper> =
        safeFlow {
            runwayClient.linkToKakao(oauthLoginRequest)
        }

    override fun unLinkToKakao(): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.unLinkToKakao()
    }

    override fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>> = safeFlow {
        runwayClient.getProfileInfoToEdit()
    }

    override fun getMyReview(page: Int, size: Int): Flow<PagingApiWrapper<MyReviewsItem>> =
        safePagingFlow {
            runwayClient.getMyReview(page, size)
        }

    override fun myReviewPaging(): Flow<PagingData<MyReviewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                MyReviewPagingSource(
                    authRepository = this
                )
            },
        ).flow
    }

    override fun bookmarkedStorePaging(): Flow<PagingData<StoreMetaDataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                BookmarkedPagingSource(
                    authRepository = this
                )
            },
        ).flow
    }

    override fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ): Flow<ApiWrapper<ImgUrlAndNicknameAndCategorys>> = safeFlow {
        runwayClient.patchProfileImage(basic, multipartFile, nickname)
    }

    override fun getBookmarkedStore(
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<StoreMetaDataItem>> = safePagingFlow {
        runwayClient.getBookmarkedStore(page, size)
    }

    companion object PreferenceKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val LOGIN_TYPE = stringPreferencesKey("login_type")
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
    }

}