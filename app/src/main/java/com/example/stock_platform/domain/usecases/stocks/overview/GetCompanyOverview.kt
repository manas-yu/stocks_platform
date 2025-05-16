package com.example.stock_platform.domain.usecases.stocks.overview

import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.repository.StocksRepository

class GetCompanyOverview(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(symbol: String): ErrorModel<OverviewResponse?> {
        return stocksRepository.getCompanyOverview(symbol)
    }
}
