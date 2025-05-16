package com.example.stock_platform.data.remote.dto

import com.example.stock_platform.domain.model.gainers_losers.MostActivelyTraded
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.google.gson.annotations.SerializedName

data class GainersLosersResponse(
    @SerializedName("last_updated") val lastUpdated: String,
    val metadata: String,
    @SerializedName("most_actively_traded") val mostActivelyTraded: List<MostActivelyTraded>,
    @SerializedName("top_gainers") val topGainers: List<TopGainer>,
    @SerializedName("top_losers") val topLosers: List<TopLoser>
)