package com.cmc12th.runway.domain.usecase

import androidx.paging.PagingData
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface GetMyProfileDataUseCase {

    fun myReviewPaging(): Flow<PagingData<MyReviewsItem>>

    fun bookmarkedStorePaging(): Flow<PagingData<StoreMetaDataItem>>

    fun convetProfileImageToMultipartFile(
        profileImage: ProfileImageType,
        argsName: String,
    ): MultipartBody.Part?
}