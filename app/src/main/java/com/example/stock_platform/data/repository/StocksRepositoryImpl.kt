package com.example.stock_platform.data.repository

import com.example.stock_platform.data.local.GainersLosersDao
import com.example.stock_platform.data.local.SearchDao
import com.example.stock_platform.data.remote.StocksApi
import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.repository.StocksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StocksRepositoryImpl(
    private val stocksApi: StocksApi,
    private val searchDao: SearchDao,
    private val gainersLosersDao: GainersLosersDao
) : StocksRepository {

    override suspend fun getTopGainersLosers(): ErrorModel<GainersLosersResponse?> {
        return try {
            val response = stocksApi.getTopGainersLosers()
                ?: return ErrorModel.Error(Exception("No data found, API limit reached"))
            ErrorModel.Success(response)
        } catch (e: Exception) {
            ErrorModel.Error(e)
        }
    }

    override suspend fun getCompanyOverview(name: String): ErrorModel<OverviewResponse?> {
        return try {
            val overview = stocksApi.getOverview(symbol = name)
                ?: return ErrorModel.Error(Exception("No data found, API limit reached"))
            ErrorModel.Success(overview)
        } catch (e: Exception) {
            ErrorModel.Error(e)
        }
    }

    override fun getSearchList(query: String): Flow<ErrorModel<List<BestMatch>?>> = flow {
        emit(ErrorModel.Loading)
        try {
            val response = stocksApi.getSearchList(keywords = query)
            if(response == null) {
                emit(ErrorModel.Error(Exception("No data found, API limit reached")))
                return@flow
            }
            emit(ErrorModel.Success(response.bestMatches))
        } catch (e: Exception) {
            emit(ErrorModel.Error(e))
        }
    }

    override fun getRecentSearches(cutoff: Long): Flow<List<BestMatch>?> {
       return searchDao.getRecentSearches(cutoff)
    }

    override suspend fun upsertSearchEntry(bestMatch: BestMatch) {
        return searchDao.upsert(bestMatch)
    }

    override suspend fun deleteOlderThan(cutoffTime: Long) {
        return searchDao.deleteOlderThan(cutoffTime)
    }

    override suspend fun getMostRecentGainersLosers(): GainersLosersResponse? {
        return gainersLosersDao.getMostRecent()
    }

    override suspend fun upsertGainersLosers(response: GainersLosersResponse) {
        return gainersLosersDao.upsert(response)
    }
}

