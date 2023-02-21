package com.cmc12th.runway.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cmc12th.runway.data.model.RecentStr

@Database(entities = [RecentStr::class], version = 1)
abstract class RecentStrDataBase : RoomDatabase() {
    abstract fun recentStrDao(): RecentStrDao
}