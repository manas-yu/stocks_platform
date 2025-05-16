package com.example.stock_platform.presentation.home

import com.example.stock_platform.domain.model.gainers_losers.StockItem
import com.example.stock_platform.domain.model.search.BestMatch

data class HomeState(
    val topGainers: List<StockItem> = emptyList(),
    val topLosers: List<StockItem> = emptyList(),
    val recentSearches: List<BestMatch> = emptyList(),
    val isGainersLosersLoading: Boolean = false,
    val isRecentSearchesLoading:Boolean = false,
    val error: String? = null
)