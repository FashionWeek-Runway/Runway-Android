package com.cmc12th.runway.data.usecase

import com.cmc12th.domain.ApiWrapper
import com.cmc12th.domain.model.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.domain.model.response.user.ImgUrlAndNickname
import com.cmc12th.domain.repository.AuthRepository
import com.cmc12th.domain.usecase.EditMyProfileUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class EditMyProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : EditMyProfileUseCase {
    override fun getProfileInfoToEdit(): Flow<ApiWrapper<ImgUrlAndNickname>> =
        authRepository.getProfileInfoToEdit()

    override fun patchProfileImage(
        basic: MultipartBody.Part?,
        multipartFile: MultipartBody.Part?,
        nickname: MultipartBody.Part?,
    ): Flow<ApiWrapper<ImgUrlAndNicknameAndCategorys>> = authRepository.patchProfileImage(
        basic = basic,
        multipartFile = multipartFile,
        nickname = nickname
    )

}