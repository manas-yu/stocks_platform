package com.example.stock_platform.domain.model.gainers_losers

import com.google.gson.annotations.SerializedName

data class MostActivelyTraded(
   @SerializedName("change_amount") val changeAmount: String,
   @SerializedName("change_percentage")   val changePercentage: String,
    val price: String,
    val ticker: String,
    val volume: String
)