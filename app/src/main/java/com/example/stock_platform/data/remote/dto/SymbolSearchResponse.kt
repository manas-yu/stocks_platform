package com.example.stock_platform.data.remote.dto

import com.example.stock_platform.domain.model.search.BestMatch

data class SymbolSearchResponse(
    val bestMatches: List<BestMatch>?
)