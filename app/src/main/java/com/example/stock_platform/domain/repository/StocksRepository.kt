package com.example.stock_platform.domain.repository

import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.search.BestMatch
import kotlinx.coroutines.flow.Flow

interface StocksRepository {
    suspend fun getTopGainersLosers(): ErrorModel<GainersLosersResponse?>
    suspend fun getCompanyOverview(name: String): ErrorModel<OverviewResponse?>
    fun getSearchList(query: String): Flow<ErrorModel<List<BestMatch>?>>
    suspend fun getRecentSearches(cutoff : Long): List<BestMatch>
    suspend fun upsertSearchEntry(bestMatch: BestMatch)
    suspend fun deleteOlderThan(cutoffTime: Long)
    suspend fun getMostRecentGainersLosers(): GainersLosersResponse?
    suspend fun upsertGainersLosers(response: GainersLosersResponse)
}
