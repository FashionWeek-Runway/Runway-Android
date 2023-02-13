package com.cmc12th.runway.ui.map

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.StoreRepository
import com.cmc12th.runway.network.RunwayClient
import com.cmc12th.runway.ui.signin.SignInUserVerificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class MapUiState(
    val onSearching: Boolean,
    val markerItems: List<NaverItem>,
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {
    private val _onSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _markerItems: MutableStateFlow<List<NaverItem>> = MutableStateFlow(emptyList())

    val mapUiState = combine(_onSearching, _markerItems) { onSearching, markerItems ->
        MapUiState(onSearching = onSearching, markerItems = markerItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignInUserVerificationUiState()
    )

    fun stores() = viewModelScope.launch {
        storeRepository.store().collect {
            Log.i("dlgocks1", it.toString())
        }
    }


    fun setMarkerItems(naverItems: List<NaverItem>) {
        _markerItems.value = naverItems
    }

    fun updateOnSearching(onSearching: Boolean) {
        _onSearching.value = onSearching
    }
}