package com.cmc12th.runway.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.network.model.safeFlow
import com.cmc12th.runway.utils.DefaultApiWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val runwayClient: RunwayClient,
) : StoreRepository {

    override fun store(): Flow<DefaultApiWrapper> = safeFlow {
        runwayClient.stores()
    }


}