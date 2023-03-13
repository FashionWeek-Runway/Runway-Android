package com.cmc12th.runway.data.usecase

import com.cmc12th.runway.data.response.store.ImgUrlAndNicknameAndCategorys
import com.cmc12th.runway.data.response.user.ImgUrlAndNickname
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.usecase.EditMyProfileUseCase
import com.cmc12th.runway.utils.ApiWrapper
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