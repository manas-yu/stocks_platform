package com.example.stock_platform.presentation.view_all

sealed class ViewAllEvent {
    object LoadMore : ViewAllEvent()
    data class StockClicked(val symbol: String) : ViewAllEvent()
    object NavigateBack : ViewAllEvent()
    object Refresh : ViewAllEvent()
}