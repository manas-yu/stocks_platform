package com.example.stock_platform.domain.usecases.stocks.gainers_losers

import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.repository.StocksRepository

class GetTopGainersLosers(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(): ErrorModel<GainersLosersResponse?> {
        return stocksRepository.getTopGainersLosers()
    }
}
