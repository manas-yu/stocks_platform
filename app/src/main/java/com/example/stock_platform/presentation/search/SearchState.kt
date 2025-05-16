package com.example.stock_platform.presentation.search

import com.example.stock_platform.domain.model.search.BestMatch

data class SearchState(
    val searchQuery: String = "",
    val stocks: List<BestMatch>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)