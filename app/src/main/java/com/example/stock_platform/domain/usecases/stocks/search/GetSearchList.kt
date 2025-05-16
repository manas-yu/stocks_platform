package com.example.stock_platform.domain.usecases.stocks.search

import com.example.stock_platform.domain.model.error_model.ErrorModel
import com.example.stock_platform.domain.model.search.BestMatch
import com.example.stock_platform.domain.repository.StocksRepository
import kotlinx.coroutines.flow.Flow

class GetSearchList(
    private val stocksRepository: StocksRepository
) {
    operator fun invoke(query: String): Flow<ErrorModel<List<BestMatch>?>> {
        return stocksRepository.getSearchList(query)
    }
}
