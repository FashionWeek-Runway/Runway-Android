package com.cmc12th.domain.repository

import com.cmc12th.domain.model.RecentStr
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearchAll(): Flow<List<RecentStr>>
    suspend fun removeSearchStr(searchStr: RecentStr)
    suspend fun removeAllSearchStr()
    suspend fun addSearchStr(recentStr: RecentStr)
}
