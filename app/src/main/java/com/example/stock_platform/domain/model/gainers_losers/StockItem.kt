package com.example.stock_platform.domain.model.gainers_losers

interface StockItem {
    val changeAmount: String
    val changePercentage: String
    val price: String
    val ticker: String
    val volume: String
}
