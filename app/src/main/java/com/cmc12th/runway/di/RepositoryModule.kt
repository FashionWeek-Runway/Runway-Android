package com.cmc12th.runway.di

import com.cmc12th.runway.data.repository.SignInRepositoryImpl
import com.cmc12th.runway.domain.repository.SignInRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsSignInRepository(
        signInRepositoryImpl: SignInRepositoryImpl
    ): SignInRepository

}