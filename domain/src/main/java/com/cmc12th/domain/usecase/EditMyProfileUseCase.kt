package com.cmc12th.domain.usecase

import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.model.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.domain.model.response.user.ImgUrlAndNickname
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