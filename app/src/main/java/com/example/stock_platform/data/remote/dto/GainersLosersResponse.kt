package com.example.stock_platform.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gainers_losers")
data class GainersLosersResponse(
    @PrimaryKey val id: Int = 0,
    @SerializedName("last_updated") val lastUpdated: String,
    val metadata: String,
    @SerializedName("top_gainers") val topGainers: List<TopGainer>,
    @SerializedName("top_losers") val topLosers: List<TopLoser>,
    val timeStamp: Long = System.currentTimeMillis()
)