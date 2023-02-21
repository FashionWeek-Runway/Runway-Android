package com.cmc12th.runway.data.roomdb.converter

import androidx.room.TypeConverter
import com.cmc12th.runway.data.model.SearchType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RecentStrConverter {
    @TypeConverter
    fun fromString(searchType: String): SearchType {
        val listType: Type = object : TypeToken<SearchType>() {}.type
        return Gson().fromJson(searchType, listType)
    }

    @TypeConverter
    fun fromSearchType(searchType: SearchType): String {
        val gson = Gson()
        return gson.toJson(searchType)
    }
}