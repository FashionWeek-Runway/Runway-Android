package com.cmc12th.runway.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.cmc12th.runway.data.request.OauthLoginRequest
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.model.ApiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getToken(type: Preferences.Key<String>): Flow<String>
    suspend fun setToken(type: Preferences.Key<String>, value: String)
    fun loginCheck(): Flow<Boolean>
}