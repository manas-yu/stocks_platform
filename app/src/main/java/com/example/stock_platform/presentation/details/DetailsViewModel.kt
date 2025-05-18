package com.example.stock_platform.presentation.details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val stockUseCases: StockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("symbol")?.let { symbol ->
            _state.value = _state.value.copy(stockSymbol = symbol)
            getCompanyOverview(symbol)
        }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.LoadStockDetails -> {
                getCompanyOverview(_state.value.stockSymbol)
            }
        }
    }

    private fun getCompanyOverview(symbol: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            val recentResult = stockUseCases.getCachedOverview(symbol)
            if (recentResult.isNotNull()) {
                _state.value = _state.value.copy(
                    stockDetails = recentResult,
                    isLoading = false
                )
                return@launch
            }
            when (val result = stockUseCases.getCompanyOverview(symbol)) {
                is ErrorModel.Success -> {
                    Log.e("DetailsViewModel", "getCompanyOverview: ${result.data}")
                    val data = result.data
                    data?.let {
                        _state.value = _state.value.copy(
                            stockDetails = data,
                            isLoading = false
                        )
                        stockUseCases.insertOverview(data.copy(timeStamp = System.currentTimeMillis()))
                    }
                }

                is ErrorModel.Error -> {
                    Log.e("DetailsViewModel", "getCompanyOverview: ${result.exception.message}")
                    _state.value = _state.value.copy(
                        error = result.exception.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            result.exception.message ?: "An unexpected error occurred"
                        )
                    )
                }

                else -> {
                    Log.e("DetailsViewModel", "getCompanyOverview: }")
                    _state.value = _state.value.copy(
                        stockDetails = null,
                        isLoading = false
                    )
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}
