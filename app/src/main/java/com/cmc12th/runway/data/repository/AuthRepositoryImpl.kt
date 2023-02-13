package com.cmc12th.runway.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.cmc12th.runway.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>,
) : AuthRepository {

    override suspend fun setToken(type: Preferences.Key<String>, value: String) {
        preferenceDataStore.edit { settings ->
            settings[type] = value
        }
    }

    override fun getToken(type: Preferences.Key<String>): Flow<String> {
        return preferenceDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { prefs ->
                prefs[type] ?: ""
            }
    }

    companion object PreferenceKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val LOGIN_TYPE = stringPreferencesKey("login_type")
        val LOGIN_CHECK = booleanPreferencesKey("login_check")
    }

}