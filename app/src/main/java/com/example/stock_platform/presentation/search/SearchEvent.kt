package com.example.stock_platform.presentation.search

sealed class SearchEvent {
    data class UpdateSearchQuery(val searchQuery: String) : SearchEvent()
    object SearchStocks : SearchEvent()
}