package com.example.stock_platform.domain.usecases.stocks

import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopGainers
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopLosers
import com.example.stock_platform.domain.usecases.stocks.overview.GetCompanyOverview
import com.example.stock_platform.domain.usecases.stocks.search.GetSearchList

data class StockUseCases(
    val getTopLosers: GetTopLosers,
    val getTopGainers: GetTopGainers,
    val getCompanyOverview: GetCompanyOverview,
    val getSearchList: GetSearchList
)