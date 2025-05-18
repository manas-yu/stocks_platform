package com.example.stock_platform.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stockUseCases: StockUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadTopGainersLosers -> {
                loadTopGainersLosers()
            }

            is HomeEvent.RefreshData -> {
                loadData()
            }

            is HomeEvent.LoadRecentSearches -> loadRecentSearches()
            is HomeEvent.SaveRecentSearch -> {
                saveRecentSearch(event.stock)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value =
                _state.value.copy(isGainersLosersLoading = true, isRecentSearchesLoading = true)
            loadTopGainersLosers()
            loadRecentSearches()
        }
    }

    private fun saveRecentSearch(stock: BestMatch) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Saving stock to recent searches: ${stock.symbol}")
            stockUseCases.upsertSearchEntry(stock.copy(timestamp = System.currentTimeMillis()))
            // No need to call loadRecentSearches() as Flow will update automatically
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRecentSearchesLoading = true)

            // Clear older searches and overviews
            stockUseCases.deleteOlderSearches()
            stockUseCases.deleteOlderOverviews()

            // Subscribe to Flow of recent searches
            stockUseCases.getRecentSearches().collect { searchResults ->
                _state.value = _state.value.copy(
                    recentSearches = searchResults,
                    isRecentSearchesLoading = false
                )
            }
        }
    }

    private fun loadTopGainersLosers() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Loading top gainers and losers")
            val recentResult = stockUseCases.getMostRecentGainersLosers()
            if (recentResult != null&&System.currentTimeMillis() - recentResult.timeStamp < 3600000) {
                Log.d(
                    "HomeViewModel",
                    "Loaded recent top gainers and losers from local database ${recentResult.topGainers}"
                )
                _state.value = _state.value.copy(
                    topGainers = recentResult.topGainers,
                    topLosers = recentResult.topLosers,
                    isGainersLosersLoading = false
                )
                return@launch
            }

            when (val networkResult = stockUseCases.getTopGainersLosers()) {
                is ErrorModel.Success -> {
                    Log.d("HomeViewModel", "Top Gainers and Losers loaded successfully")
                    val data = networkResult.data
                    if (data != null) {
                        _state.value = _state.value.copy(
                            topGainers = data.topGainers,
                            topLosers = data.topLosers,
                            isGainersLosersLoading = false
                        )
                        stockUseCases.upsertGainersLosers(data.copy(timeStamp = System.currentTimeMillis()))
                    } else {
                        _state.value = _state.value.copy(
                            error = "An unexpected error occurred",
                            isGainersLosersLoading = false
                        )
                    }
                }

                is ErrorModel.Error -> {
                    Log.e(
                        "HomeViewModel",
                        "Error loading top gainers and losers: ${networkResult.exception.message}"
                    )
                    _state.value = _state.value.copy(
                        error = "An unexpected error occurred",
                        isGainersLosersLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            _state.value.error ?: "Unknown error"
                        )
                    )
                }

                else -> {
                    Log.e("HomeViewModel", "Error loading top gainers and losers: Unknown error")
                    _state.value = _state.value.copy(
                        topGainers = emptyList(),
                        isGainersLosersLoading = false
                    )
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}