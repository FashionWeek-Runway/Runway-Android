package com.cmc12th.domain.model

import com.cmc12th.domain.model.response.ErrorResponse

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
        if (this is NotResponse) {
            onError(ErrorResponse("500", false, "네트워크 오류가 발생했습니다."))
        }
    }

    fun onLoading(onLoading: () -> Unit) {
        if (this is Loading) {
            onLoading()
        }
    }

}