package com.example.stock_platform.domain.usecases.stocks.gainers_losers

import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.example.stock_platform.domain.repository.StocksRepository

class GetTopLosers(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(): ErrorModel<List<TopLoser>> {
        return stocksRepository.getTopLosers()
    }
}
