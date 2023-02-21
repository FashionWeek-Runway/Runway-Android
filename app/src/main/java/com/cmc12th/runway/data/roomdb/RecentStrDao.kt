package com.cmc12th.runway.data.roomdb

import androidx.room.*
import com.cmc12th.runway.data.model.RecentStr
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentStrDao {

    @Query("SELECT * FROM recentStr ORDER BY id DESC")
    fun getRecentStrAll(): Flow<List<RecentStr>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentStr: RecentStr)

    @Update
    suspend fun update(recentStr: RecentStr)

    @Delete
    suspend fun delete(recentStr: RecentStr)

    @Query("DELETE FROM recentStr")
    suspend fun deleteAll()
}