package com.example.stock_platform.domain.usecases.stocks.search

import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.repository.StocksRepository
import kotlinx.coroutines.flow.Flow

class GetRecentSearches(
    private val stocksRepository: StocksRepository
) {
    operator fun invoke(): Flow<List<BestMatch>> {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        return stocksRepository.getRecentSearches(oneHourAgo)
    }
}