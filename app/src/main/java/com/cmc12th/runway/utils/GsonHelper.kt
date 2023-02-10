package com.cmc12th.runway.utils

import com.cmc12th.runway.data.response.ErrorResponse
import com.google.gson.Gson

object GsonHelper {
    private val gson = Gson()
    
    fun stringToErrorResponse(errorBody: String): ErrorResponse {
        return gson.fromJson(errorBody, ErrorResponse::class.java)
    }
}