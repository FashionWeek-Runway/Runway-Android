package com.cmc12th.runway.data.usecase

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.runway.data.pagingsource.BookmarkedPagingSource
import com.cmc12th.runway.data.pagingsource.MyReviewPagingSource
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.usecase.GetMyProfileDataUseCase
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import com.cmc12th.runway.utils.fileFromContentUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class GetMyProfileDataUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : GetMyProfileDataUseCase {
    override fun myReviewPaging(): Flow<PagingData<MyReviewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                MyReviewPagingSource(
                    authRepository = authRepository
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
                    authRepository = authRepository
                )
            },
        ).flow
    }

    override fun convetProfileImageToMultipartFile(
        profileImage: ProfileImageType,
        argsName: String,
    ): MultipartBody.Part? =
        when (profileImage) {
            ProfileImageType.DEFAULT -> null
            is ProfileImageType.SOCIAL -> null
            is ProfileImageType.LOCAL -> {
                val file = fileFromContentUri(context, profileImage.uri)
                val requestBody: RequestBody = file.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
            }
        }
}