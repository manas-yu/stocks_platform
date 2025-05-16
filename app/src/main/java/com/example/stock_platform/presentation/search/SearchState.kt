package com.example.stock_platform.presentation.search

import com.example.stock_platform.domain.model.search.BestMatch
import kotlinx.coroutines.flow.Flow

data class SearchState(
    val searchQuery: String = "",
    val stocks: Flow<List<BestMatch>>? = null
)