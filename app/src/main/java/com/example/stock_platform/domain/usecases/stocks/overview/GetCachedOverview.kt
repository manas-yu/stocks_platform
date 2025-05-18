package com.example.stock_platform.domain.usecases.stocks.overview

import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.repository.StocksRepository

class GetCachedOverview(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(symbol: String): OverviewResponse? {
        return stocksRepository.getOverview(symbol)
    }
}