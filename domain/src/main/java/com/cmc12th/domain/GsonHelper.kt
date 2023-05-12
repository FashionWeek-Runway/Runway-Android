package com.cmc12th.domain

import com.cmc12th.domain.model.response.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonHelper {
    val gson = Gson()

    inline fun <reified T> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun stringToErrorResponse(errorBody: String): com.cmc12th.domain.model.response.ErrorResponse {
        return gson.fromJson(errorBody, com.cmc12th.domain.model.response.ErrorResponse::class.java)
            .copy(
                totalResponse = errorBody
            )
    }
}

