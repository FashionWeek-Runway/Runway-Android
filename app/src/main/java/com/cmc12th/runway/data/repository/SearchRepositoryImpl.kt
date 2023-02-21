package com.cmc12th.runway.data.repository

import com.cmc12th.runway.data.model.RecentStr
import com.cmc12th.runway.data.roomdb.RecentStrDao
import com.cmc12th.runway.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val recentStrDao: RecentStrDao,
) : SearchRepository {
    override fun getRecentSearchAll(): Flow<List<RecentStr>> = recentStrDao.getRecentStrAll()

    override suspend fun addSearchStr(recentStr: RecentStr) {
        return recentStrDao.insert(recentStr)
    }

    override suspend fun removeSearchStr(recentStr: RecentStr) {
        return recentStrDao.delete(recentStr)
    }

    override suspend fun removeAllSearchStr() {
        return recentStrDao.deleteAll()
    }
}