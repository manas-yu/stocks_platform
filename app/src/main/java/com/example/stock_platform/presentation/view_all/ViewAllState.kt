package com.example.stock_platform.presentation.view_all

import com.example.stock_platform.domain.model.gainers_losers.StockItem
import com.example.stock_platform.domain.model.search.BestMatch

data class ViewAllState(
    val isLoading: Boolean = false,
    val viewAllType: ViewAllType? = null,
    val error: String = "",
    val recentSearches: List<BestMatch> = emptyList(),
    val topGainers: List<StockItem> = emptyList(),
    val topLosers: List<StockItem> = emptyList(),
    val pageSize: Int = 10,
    val displayItemCount: Int = 10
)

enum class ViewAllType {
    RECENT_SEARCHES,
    TOP_LOSERS,
    TOP_GAINERS
}