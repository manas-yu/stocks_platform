package com.example.stock_platform.domain.usecases.stocks

import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopGainersLosers
import com.example.stock_platform.domain.usecases.stocks.overview.GetCompanyOverview
import com.example.stock_platform.domain.usecases.stocks.search.GetSearchList

data class StockUseCases(
    val getTopGainersLosers: GetTopGainersLosers,
    val getCompanyOverview: GetCompanyOverview,
    val getSearchList: GetSearchList
)