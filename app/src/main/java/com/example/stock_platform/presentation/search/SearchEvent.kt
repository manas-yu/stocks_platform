package com.example.stock_platform.presentation.search

import com.example.stock_platform.domain.model.search.BestMatch

sealed class SearchEvent {
    data class UpdateSearchQuery(val searchQuery: String) : SearchEvent()
    object SearchStocks : SearchEvent()
}