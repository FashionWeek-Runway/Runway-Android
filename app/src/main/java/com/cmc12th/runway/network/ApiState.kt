package com.cmc12th.runway.network

import com.cmc12th.runway.data.response.ErrorResponse


sealed class ApiState<out T : Any> {
    data class Success<T : Any>(val data: T) : ApiState<T>()
    data class Error(val errorResponse: ErrorResponse) : ApiState<Nothing>()
    data class NotResponse(val message: String?, val exception: Throwable? = null) :
        ApiState<Nothing>()

    object Loading : ApiState<Nothing>()

    fun onSuccess(onSuccess: (T) -> Unit) {
        if (this is Success) {
            onSuccess(this@ApiState.data)
        }
    }

    fun onError(onError: (ErrorResponse) -> Unit) {
        if (this is Error) {
            onError(this@ApiState.errorResponse)
        }
    }

    fun onNotResponse(onNotResponse: () -> Unit) {
        if (this is NotResponse) {
            onNotResponse()
        }
    }

    fun onLoading(onLoading: () -> Unit) {
        if (this is Loading) {
            onLoading()
        }
    }

}