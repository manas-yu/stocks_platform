package com.example.stock_platform.domain.usecases.stocks.gainers_losers

import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.repository.StocksRepository

class GetTopGainers(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(): ErrorModel<List<TopGainer>> {
        return stocksRepository.getTopGainers()
    }
}
