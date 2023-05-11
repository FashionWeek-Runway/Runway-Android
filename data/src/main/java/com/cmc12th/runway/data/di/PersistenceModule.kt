package com.cmc12th.runway.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.cmc12th.runway.data.Constants.RECENT_STR_DATABASE
import com.cmc12th.runway.data.Constants.RUNWAY_DATASTORE
import com.cmc12th.runway.data.roomdb.RecentStrDao
import com.cmc12th.runway.data.roomdb.RecentStrDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(RUNWAY_DATASTORE) },
        )

    @Provides
    @Singleton
    fun provideRecentStrDataBase(
        application: Application,
    ): RecentStrDataBase {
        return Room.databaseBuilder(application, RecentStrDataBase::class.java, RECENT_STR_DATABASE)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRecentStrDao(recentStrDataBase: RecentStrDataBase): RecentStrDao {
        return recentStrDataBase.recentStrDao()
    }
}