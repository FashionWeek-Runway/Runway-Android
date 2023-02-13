package com.cmc12th.runway.domain.repository

import com.cmc12th.runway.utils.DefaultApiWrapper
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun store(): Flow<DefaultApiWrapper>
}