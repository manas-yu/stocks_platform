package com.example.stock_platform.presentation.view_all

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewAllViewModel @Inject constructor(
    private val stockUseCases: StockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ViewAllState())
    val state: State<ViewAllState> = _state

    init {
        savedStateHandle.get<String>("viewAllType")?.let {
            _state.value = _state.value.copy(
                viewAllType = ViewAllType.valueOf(it)
            )
        }
        loadData()
    }

    fun onEvent(event: ViewAllEvent) {
        when (event) {
            is ViewAllEvent.LoadMore -> {
                _state.value = _state.value.copy(
                    displayItemCount = _state.value.displayItemCount + _state.value.pageSize
                )
            }

            is ViewAllEvent.Refresh -> {
                loadData()
            }

            is ViewAllEvent.StockClicked, is ViewAllEvent.NavigateBack -> {
                // These events will be handled in the UI layer
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            if (_state.value.viewAllType == ViewAllType.RECENT_SEARCHES)
                loadRecentSearches()
            else
                loadTopGainersLosers()
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Get the current value of recent searches
            val searchResults = stockUseCases.getRecentSearches().first()
            if (searchResults.isEmpty()) {
                _state.value = _state.value.copy(
                    recentSearches = emptyList(),
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    recentSearches = searchResults,
                    isLoading = false,
                    error = ""
                )
            }
        }
    }

    private fun loadTopGainersLosers() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Loading top gainers and losers")
            val recentResult = stockUseCases.getMostRecentGainersLosers()

            if (_state.value.viewAllType == ViewAllType.TOP_GAINERS) {
                if (recentResult != null) {
                    if (recentResult.topGainers!!.isEmpty()) {
                        _state.value = _state.value.copy(
                            error = "No Top Gainers Found",
                            isLoading = false
                        )
                    } else {
                        _state.value = _state.value.copy(
                            topGainers = recentResult.topGainers,
                            isLoading = false,
                            error = ""
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        error = "Failed to load Top Gainers",
                        isLoading = false
                    )
                }
            } else {
                if (recentResult != null) {
                    if (recentResult.topLosers!!.isEmpty()) {
                        _state.value = _state.value.copy(
                            error = "No Top Losers Found",
                            isLoading = false
                        )
                    } else {
                        _state.value = _state.value.copy(
                            topLosers = recentResult.topLosers,
                            isLoading = false,
                            error = ""
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        error = "Failed to load Top Losers",
                        isLoading = false
                    )
                }
            }
        }
    }
}
