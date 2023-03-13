package com.cmc12th.runway.domain.usecase

import com.cmc12th.runway.data.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.runway.data.response.user.ImgUrlAndNickname
import com.cmc12th.runway.utils.ApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface EditMyProfileUseCase {

    fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>>

    fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ): Flow<ApiWrapper<ImgUrlAndNicknameAndCategorys>>
}