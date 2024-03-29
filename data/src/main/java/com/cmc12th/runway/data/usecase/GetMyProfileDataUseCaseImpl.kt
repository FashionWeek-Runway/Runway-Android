package com.cmc12th.runway.data.usecase

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cmc12th.domain.model.response.user.MyReviewsItem
import com.cmc12th.domain.model.response.user.StoreMetaDataItem
import com.cmc12th.domain.model.signin.ProfileImageType
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.usecase.GetMyProfileDataUseCase
import com.cmc12th.runway.data.fileFromContentUri
import com.cmc12th.runway.data.pagingsource.BookmarkedPagingSource
import com.cmc12th.runway.data.pagingsource.BookmarkedReviewPagingSource
import com.cmc12th.runway.data.pagingsource.MyReviewPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class GetMyProfileDataUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val context: Context,
    private val ioDispatcher: kotlinx.coroutines.CoroutineDispatcher,
) : GetMyProfileDataUseCase {
    override fun myReviewPaging(): Flow<PagingData<MyReviewsItem>> = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            MyReviewPagingSource(
                authRepository = authRepository
            )
        },
    ).flow.flowOn(ioDispatcher)

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

    override fun bookmarkedReviewPaging(): Flow<PagingData<MyReviewsItem>> = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            BookmarkedReviewPagingSource(
                authRepository = authRepository
            )
        },
    ).flow.flowOn(ioDispatcher)

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