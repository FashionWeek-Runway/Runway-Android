package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.data.response.PagingResponse
import com.cmc12th.runway.data.response.map.MapFilterItem
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.utils.ApiWrapper
import com.cmc12th.runway.utils.DefaultApiWrapper
import com.cmc12th.runway.utils.PagingApiWrapper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.*

interface StoreRepository {
    fun store(): Flow<DefaultApiWrapper>

    fun getBlogReview(storeId: Int, storeName: String): Flow<ApiWrapper<List<BlogReview>>>

    fun getDetail(storeId: Int): Flow<ApiWrapper<StoreDetail>>

    fun getUserReview(
        storeId: Int,
        page: Int,
        size: Int,
    ): Flow<PagingApiWrapper<UserReview>>

    fun writeUserReview(
        @Path("storeId") storeId: Int,
        @Part img: MultipartBody.Part,
    ): Flow<DefaultApiWrapper>
}