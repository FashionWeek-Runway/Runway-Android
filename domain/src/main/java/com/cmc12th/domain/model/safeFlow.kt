package com.cmc12th.domain.model

import com.cmc12th.domain.GsonHelper
import com.cmc12th.domain.model.response.PagingResponse
import com.cmc12th.domain.model.response.ResponseWrapper
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

fun <T : Any> safePagingFlow(apiFunc: suspend () -> Response<PagingResponse<T>>): Flow<ApiState<PagingResponse<T>>> =
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