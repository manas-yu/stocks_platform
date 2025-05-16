package com.example.stock_platform.presentation.home

import com.example.stock_platform.domain.model.gainers_losers.StockItem

data class HomeState(
    val topGainers: List<StockItem> = emptyList(),
    val topLosers: List<StockItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)