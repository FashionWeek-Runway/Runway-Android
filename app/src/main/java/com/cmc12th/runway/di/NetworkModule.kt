package com.cmc12th.runway.di

import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.ServiceInterceptor
import com.cmc12th.runway.network.model.TokenAuthenticator
import com.cmc12th.runway.network.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val DEV_SERVER = "https://dev.runwayserver.shop/"
    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RunwayInterceptorOkhttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherInterceptorOkhttpClinet

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RunwayRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OtherRetrofit

    @RunwayInterceptorOkhttpClient
    @Provides
    fun provideRunwayInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(TokenAuthenticator())
            .addInterceptor(ServiceInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @RunwayRetrofit
    fun provideRunwayRetrofit(
        @RunwayInterceptorOkhttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DEV_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginService(
        @RunwayRetrofit retrofit: Retrofit,
    ): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        @RunwayRetrofit retrofit: Retrofit,
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreService(
        @RunwayRetrofit retrofit: Retrofit,
    ): StoreService {
        return retrofit.create(StoreService::class.java)
    }


    @Provides
    @Singleton
    fun provideMapService(
        @RunwayRetrofit retrofit: Retrofit,
    ): MapService {
        return retrofit.create(MapService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeService(
        @RunwayRetrofit retrofit: Retrofit,
    ): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @Provides
    @Singleton
    fun provideRunwayClient(
        loginService: LoginService,
        authService: AuthService,
        storeService: StoreService,
        mapService: MapService,
        homeService: HomeService,
    ): RunwayClient {
        return RunwayClient(
            loginService = loginService,
            authService = authService,
            storeService = storeService,
            mapService = mapService,
            homeService = homeService
        )
    }

}