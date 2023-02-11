package com.cmc12th.runway.utils

import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.ApiState
import retrofit2.Response

typealias DefaultResponse = Response<ResponseWrapper<String>>
typealias NetworkResponse<T> = Response<ResponseWrapper<T>>

typealias ApiWrapper<T> = ApiState<ResponseWrapper<T>>
typealias DefaultApiWrapper = ApiState<ResponseWrapper<String>>