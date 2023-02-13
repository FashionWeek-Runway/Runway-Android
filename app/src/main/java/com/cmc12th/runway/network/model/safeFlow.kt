package com.cmc12th.runway.network.model

import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.utils.GsonHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T : Any> safeFlow(apiFunc: suspend () -> Response<ResponseWrapper<T>>): Flow<ApiState<ResponseWrapper<T>>> =
    flow {
        try {
            val res = apiFunc.invoke()
            if (res.isSuccessful) {
                emit(ApiState.Success(res.body() ?: throw NullPointerException()))
            } else {
                val errorBody = res.errorBody() ?: throw NullPointerException()
                emit(ApiState.Error(GsonHelper.stringToErrorResponse(errorBody = errorBody.string())))
            }
        } catch (e: Exception) {
            emit(ApiState.NotResponse(message = e.message ?: "", exception = e))
        }
    }.flowOn(Dispatchers.IO)