package com.example.stock_platform.data.repository

import com.example.stock_platform.data.remote.StocksApi
import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.repository.StocksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StocksRepositoryImpl(private val stocksApi: StocksApi) : StocksRepository {

    override suspend fun getTopGainers(): ErrorModel<List<TopGainer>> {
        return try {
            val response = stocksApi.getTopGainersLosers()
            ErrorModel.Success(response.topGainers)
        } catch (e: Exception) {
            ErrorModel.Error(e)
        }
    }

    override suspend fun getTopLosers(): ErrorModel<List<TopLoser>> {
        return try {
            val response = stocksApi.getTopGainersLosers()
            ErrorModel.Success(response.topLosers)
        } catch (e: Exception) {
            ErrorModel.Error(e)
        }
    }

    override suspend fun getCompanyOverview(name: String): ErrorModel<OverviewResponse> {
        return try {
            val overview = stocksApi.getOverview(symbol = name)
            ErrorModel.Success(overview)
        } catch (e: Exception) {
            ErrorModel.Error(e)
        }
    }

    override fun getSearchList(query: String): Flow<ErrorModel<List<BestMatch>>> = flow {
        emit(ErrorModel.Loading)
        try {
            val response = stocksApi.getSearchList(keywords = query)
            emit(ErrorModel.Success(response.bestMatches))
        } catch (e: Exception) {
            emit(ErrorModel.Error(e))
        }
    }
}

