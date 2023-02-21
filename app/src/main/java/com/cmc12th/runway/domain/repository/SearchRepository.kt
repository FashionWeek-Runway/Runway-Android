package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.model.RecentStr
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearchAll(): Flow<List<RecentStr>>
    suspend fun removeSearchStr(searchStr: RecentStr)
    suspend fun removeAllSearchStr()
    suspend fun addSearchStr(recentStr: RecentStr)
}
