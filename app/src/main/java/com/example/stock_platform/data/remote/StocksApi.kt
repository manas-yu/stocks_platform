package com.example.stock_platform.data.remote

import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.data.remote.dto.SymbolSearchResponse
import com.example.stock_platform.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query


interface StocksApi {
    @GET("query")
    suspend fun getSearchList(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = API_KEY
    ): SymbolSearchResponse?

    @GET("query")
    suspend fun getTopGainersLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = API_KEY
    ): GainersLosersResponse?

    @GET("query")
    suspend fun getOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): OverviewResponse?
}