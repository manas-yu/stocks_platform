package com.example.stock_platform.domain.usecases.stocks.gainers_losers

import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.domain.repository.StocksRepository

class UpsertGainersLosers(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(gainersLosersResponse: GainersLosersResponse) {
        stocksRepository.upsertGainersLosers(gainersLosersResponse)
    }
}