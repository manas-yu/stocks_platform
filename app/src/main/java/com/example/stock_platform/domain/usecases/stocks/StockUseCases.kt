package com.example.stock_platform.domain.usecases.stocks

import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetMostRecentGainersLosers
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.GetTopGainersLosers
import com.example.stock_platform.domain.usecases.stocks.gainers_losers.UpsertGainersLosers
import com.example.stock_platform.domain.usecases.stocks.overview.DeleteOlderOverviews
import com.example.stock_platform.domain.usecases.stocks.overview.GetCachedOverview
import com.example.stock_platform.domain.usecases.stocks.overview.GetCompanyOverview
import com.example.stock_platform.domain.usecases.stocks.overview.InsertOverview
import com.example.stock_platform.domain.usecases.stocks.search.DeleteOlderSearches
import com.example.stock_platform.domain.usecases.stocks.search.GetRecentSearches
import com.example.stock_platform.domain.usecases.stocks.search.GetSearchList
import com.example.stock_platform.domain.usecases.stocks.search.UpsertSearchEntry

data class StockUseCases(
    val getTopGainersLosers: GetTopGainersLosers,
    val getCompanyOverview: GetCompanyOverview,
    val getSearchList: GetSearchList,
    val getRecentSearches: GetRecentSearches,
    val upsertSearchEntry: UpsertSearchEntry,
    val deleteOlderSearches: DeleteOlderSearches,
    val getMostRecentGainersLosers: GetMostRecentGainersLosers,
    val upsertGainersLosers: UpsertGainersLosers,
    val getCachedOverview : GetCachedOverview,
    val insertOverview : InsertOverview,
    val deleteOlderOverviews: DeleteOlderOverviews
)