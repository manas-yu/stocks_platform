package com.example.stock_platform.domain.repository

import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.example.stock_platform.domain.model.search.BestMatch
import kotlinx.coroutines.flow.Flow

interface StocksRepository {
    suspend fun getTopGainers(): ErrorModel<List<TopGainer>>
    suspend fun getTopLosers(): ErrorModel<List<TopLoser>>
    suspend fun getCompanyOverview(name: String): ErrorModel<OverviewResponse>
    fun getSearchList(query: String): Flow<ErrorModel<List<BestMatch>>>
}
