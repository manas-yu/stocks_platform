package com.example.stock_platform.presentation.home

import com.example.stock_platform.domain.model.search.BestMatch

sealed class HomeEvent {
    object LoadTopGainersLosers : HomeEvent()
    object RefreshData : HomeEvent()
    object LoadRecentSearches : HomeEvent()
    data class SaveRecentSearch(val stock: BestMatch) : HomeEvent()
}