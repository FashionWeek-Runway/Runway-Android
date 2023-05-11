package com.cmc12th.domain.usecase

import androidx.paging.PagingData
import com.cmc12th.domain.model.response.user.MyReviewsItem
import com.cmc12th.domain.model.response.user.StoreMetaDataItem
import com.cmc12th.domain.model.signin.ProfileImageType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface GetMyProfileDataUseCase {

    fun myReviewPaging(): Flow<PagingData<MyReviewsItem>>

    fun bookmarkedStorePaging(): Flow<PagingData<StoreMetaDataItem>>

    fun bookmarkedReviewPaging(): Flow<PagingData<MyReviewsItem>>

    fun convetProfileImageToMultipartFile(
        profileImage: ProfileImageType,
        argsName: String,
    ): MultipartBody.Part?
}