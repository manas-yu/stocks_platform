package com.example.stock_platform.presentation.details

import com.example.stock_platform.data.remote.dto.OverviewResponse

data class DetailsState(
    val stockSymbol: String = "",
    val stockDetails: OverviewResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)