package com.example.stock_platform.domain.usecases.stocks.search

import com.example.stock_platform.data.local.SearchDao
import com.example.stock_platform.domain.repository.StocksRepository

class DeleteOlderSearches(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke() {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        stocksRepository.deleteOlderThan(oneHourAgo)
    }
}