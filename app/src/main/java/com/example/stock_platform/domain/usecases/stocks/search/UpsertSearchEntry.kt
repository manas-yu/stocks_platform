package com.example.stock_platform.domain.usecases.stocks.search

import com.example.stock_platform.data.local.SearchDao
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.repository.StocksRepository

class UpsertSearchEntry(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(bestMatch: BestMatch) {
        stocksRepository.upsertSearchEntry(bestMatch)
    }
}