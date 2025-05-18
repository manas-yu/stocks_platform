package com.example.stock_platform.domain.usecases.stocks.overview

import com.example.stock_platform.domain.repository.StocksRepository

class DeleteOlderOverviews(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke() {
        val oneHourAgo = System.currentTimeMillis() - 60 * 60 * 1000
        stocksRepository.deleteOlderOverviews(oneHourAgo)
    }
}