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
            _state.value = _state.value.copy(isLoading = true)
            loadTopGainersLosers()
        }
    }

    private fun loadTopGainersLosers() {
        viewModelScope.launch {
            when (val result = stockUseCases.getTopGainersLosers()) {
                is ErrorModel.Success -> {
                    val data = result.data
                    _state.value = _state.value.copy(
                        topGainers = data!!.topGainers,
                        topLosers = data.topLosers,
                        isLoading = false
                    )
                }
                is ErrorModel.Error -> {
                    _state.value = _state.value.copy(
                        error = result.exception.localizedMessage ?: "An unexpected error occurred",
                        isLoading = false
                    )
                    _eventFlow.emit(UIEvent.ShowSnackbar(
                        _state.value.error ?: "Unknown error"
                    ))
                }
                else -> {
                    _state.value = _state.value.copy(
                        topGainers = emptyList(),
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