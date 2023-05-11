package com.cmc12th.domain

import com.cmc12th.domain.model.ApiState
import com.cmc12th.domain.model.response.PagingResponse
import com.cmc12th.domain.model.response.ResponseWrapper
import retrofit2.Response

typealias DefaultResponse = Response<ResponseWrapper<String>>
typealias NetworkResponse<T> = Response<ResponseWrapper<T>>
typealias PagingNetworkResponse<T> = Response<PagingResponse<T>>

typealias ApiWrapper<T> = ApiState<ResponseWrapper<T>>
typealias PagingApiWrapper<T> = ApiState<PagingResponse<T>>
typealias DefaultApiWrapper = ApiState<ResponseWrapper<String>>
