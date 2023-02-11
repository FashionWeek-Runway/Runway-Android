package com.cmc12th.runway.utils

import android.util.Log
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.data.response.NotRegisteredResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonHelper {
    val gson = Gson()

    inline fun <reified T> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun stringToErrorResponse(errorBody: String): ErrorResponse {
        return gson.fromJson(errorBody, ErrorResponse::class.java).copy(
            totalResponse = errorBody
        )
    }
}

