package com.cmc12th.runway.ui.map

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc12th.runway.data.model.NaverItem
import com.cmc12th.runway.ui.signin.SignInUserVerificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
data class MapUiState(
    val onSearching: Boolean,
    val markerItems: List<NaverItem>,
)

@HiltViewModel
class MapViewModel @Inject constructor(

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

    fun setMarkerItems(naverItems: List<NaverItem>) {
        _markerItems.value = naverItems
    }

    fun updateOnSearching(onSearching: Boolean) {
        _onSearching.value = onSearching
    }
}