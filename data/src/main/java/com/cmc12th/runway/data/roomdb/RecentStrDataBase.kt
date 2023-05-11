package com.cmc12th.runway.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cmc12th.domain.model.RecentStr
import com.cmc12th.runway.data.roomdb.converter.RecentStrConverter

@Database(entities = [RecentStr::class], version = 1)
@TypeConverters(RecentStrConverter::class)
abstract class RecentStrDataBase : RoomDatabase() {
    abstract fun recentStrDao(): RecentStrDao
}