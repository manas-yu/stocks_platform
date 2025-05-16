package com.example.stock_platform.domain.model.gainers_losers

import com.google.gson.annotations.SerializedName

data class TopGainer(
    @SerializedName("change_amount") override val changeAmount: String,
    @SerializedName("change_percentage") override val changePercentage: String,
    override val price: String,
    override val ticker: String,
    override val volume: String
):StockItem