package com.cmc12th.runway.di

import com.cmc12th.runway.data.repository.AuthRepositoryImpl
import com.cmc12th.runway.data.repository.MapRepositoryImpl
import com.cmc12th.runway.data.repository.SignInRepositoryImpl
import com.cmc12th.runway.data.repository.StoreRepositoryImpl
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.MapRepository
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.domain.repository.StoreRepository
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
        signInRepositoryImpl: SignInRepositoryImpl,
    ): SignInRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl,
    ): StoreRepository

    @Binds
    @ViewModelScoped
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapRepositoryImpl
    ): MapRepository
}