package com.example.stock_platform.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private fun loadRecentSearches() {
        stockUseCases.getRecentSearches().onEach { result ->
            if (result == null) {
                _state.value = _state.value.copy(
                    recentSearches = emptyList(),
                    isRecentSearchesLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    recentSearches = result,
                    isRecentSearchesLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun loadTopGainersLosers() {
        viewModelScope.launch {
            when (val result = stockUseCases.getTopGainersLosers()) {
                is ErrorModel.Success -> {
                    Log.d("HomeViewModel", "Top Gainers and Losers loaded successfully")
                    val data = result.data
                    if (data != null) {
                        _state.value = _state.value.copy(
                            topGainers = data.topGainers,
                            topLosers = data.topLosers,
                            isGainersLosersLoading = false
                        )
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
                        "Error loading top gainers and losers: ${result.exception.message}"
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