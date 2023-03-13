package com.cmc12th.runway.di

import android.content.Context
import com.cmc12th.runway.data.usecase.EditMyProfileUseCaseImpl
import com.cmc12th.runway.data.usecase.GetMyProfileDataUseCaseImpl
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.usecase.EditMyProfileUseCase
import com.cmc12th.runway.domain.usecase.GetMyProfileDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetMyProfileDataUseCase(
        authRepository: AuthRepository,
        @ApplicationContext context: Context,
        @DispatcherModule.IoDispatcher ioDispatcher: kotlinx.coroutines.CoroutineDispatcher,
    ): GetMyProfileDataUseCase {
        return GetMyProfileDataUseCaseImpl(
            authRepository = authRepository,
            context = context,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @ViewModelScoped
    fun provideEditMyProfileDataUseCase(
        authRepository: AuthRepository,
    ): EditMyProfileUseCase {
        return EditMyProfileUseCaseImpl(
            authRepository = authRepository,
        )
    }
    
}