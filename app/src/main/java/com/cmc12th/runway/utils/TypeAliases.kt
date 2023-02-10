package com.cmc12th.runway.utils

import com.cmc12th.runway.data.response.ResponseWrapper
import retrofit2.Response

typealias DefaultResponse = Response<ResponseWrapper<String>>
typealias NetworkResponse<T> = Response<ResponseWrapper<T>>