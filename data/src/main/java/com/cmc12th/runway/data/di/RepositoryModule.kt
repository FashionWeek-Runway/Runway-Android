package com.cmc12th.runway.data.di

import com.cmc12th.domain.repository.*
import com.cmc12th.runway.data.repository.*
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
        mapRepositoryImpl: MapRepositoryImpl,
    ): MapRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl,
    ): SearchRepository

    @Binds
    @ViewModelScoped
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl,
    ): HomeRepository
}