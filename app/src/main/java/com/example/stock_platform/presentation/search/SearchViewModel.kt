package com.example.stock_platform.presentation.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val stockUseCases: StockUseCases
) : ViewModel() {
    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchStocks -> {
                searchStocks()
            }

            is SearchEvent.UpdateSearchQuery -> {
                _state.value = _state.value.copy(searchQuery = event.searchQuery)
            }
        }
    }

    private fun searchStocks() {
        if (_state.value.searchQuery.isBlank()) {
            _state.value = _state.value.copy(
                stocks = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }

        stockUseCases.getSearchList(query = _state.value.searchQuery)
            .onEach { result ->
                when (result) {
                    is ErrorModel.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is ErrorModel.Success -> {
                        val sortedStocks = result.data?.sortedByDescending {
                            it.matchScore.toDoubleOrNull() ?: 0.0
                        } ?: emptyList()

                        _state.value = _state.value.copy(
                            stocks = sortedStocks,
                            isLoading = false,
                            error = null
                        )
                    }
                    is ErrorModel.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }
}
