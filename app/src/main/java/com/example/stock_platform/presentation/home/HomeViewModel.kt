package com.example.stock_platform.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.gainers_losers.StockItem
import com.example.stock_platform.domain.usecases.stocks.StockUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

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
            is HomeEvent.LoadTopGainers -> {
                loadTopGainers()
            }
            is HomeEvent.LoadTopLosers -> {
                loadTopLosers()
            }
            is HomeEvent.RefreshData -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            loadTopGainers()
            loadTopLosers()
        }
    }

    private fun loadTopGainers() {
        viewModelScope.launch {
            when (val result = stockUseCases.getTopGainers()) {
                is ErrorModel.Success -> {
                    Log.e(  "TAG", "loadTopGainers: ${result.data}")
                    val data = result.data
                    _state.value = _state.value.copy(
                        topGainers = data,
                        isLoading = false
                    )
                }
                is ErrorModel.Error -> {
                    Log.e("TAG", "loadTopGainers: ${result.exception}")
                    _state.value = _state.value.copy(
                        error = result.exception.localizedMessage ?: "An unexpected error occurred",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackbar(
                        _state.value.error ?: "Unknown error"
                    ))
                }
                else -> {
                    Log.e("TAG", "loadTopGainers: $result")
                    // Handle other cases if needed
                    _state.value = _state.value.copy(
                        topGainers = emptyList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadTopLosers() {
        viewModelScope.launch {

            when (val result = stockUseCases.getTopLosers()) {
                is ErrorModel.Success -> {
                    Log.e("TAG", "loadTopLosers: ${result.data}")
                    val data = result.data
                    _state.value = _state.value.copy(
                        topLosers = data,
                        isLoading = false
                    )
                }
                is ErrorModel.Error -> {
                    Log.e("TAG", "loadTopLosers: ${result.exception}")
                    _state.value = _state.value.copy(
                        error = result.exception.localizedMessage ?: "An unexpected error occurred",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackbar(
                        _state.value.error ?: "Unknown error"
                    ))
                }
                else -> {
                    Log.e("TAG", "loadTopLosers: $result")
                    // Handle other cases if needed
                    _state.value = _state.value.copy(
                        topLosers = emptyList(),
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