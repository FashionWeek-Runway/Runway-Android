package com.cmc12th.runway.utils

import com.cmc12th.runway.data.response.PagingMetadata
import com.cmc12th.runway.data.response.PagingResponse
import com.cmc12th.runway.data.response.ResponseWrapper
import com.cmc12th.runway.network.model.ApiState
import retrofit2.Response

typealias DefaultResponse = Response<ResponseWrapper<String>>
typealias NetworkResponse<T> = Response<ResponseWrapper<T>>
typealias PagingNetworkResponse<T> = Response<PagingResponse<T>>

typealias ApiWrapper<T> = ApiState<ResponseWrapper<T>>
typealias PagingApiWrapper<T> = ApiState<PagingResponse<T>>
typealias DefaultApiWrapper = ApiState<ResponseWrapper<String>>
