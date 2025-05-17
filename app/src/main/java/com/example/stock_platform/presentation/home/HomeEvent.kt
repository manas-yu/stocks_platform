package com.example.stock_platform.presentation.home

sealed class HomeEvent {
     object LoadTopGainersLosers : HomeEvent()
     object RefreshData : HomeEvent()
     object LoadRecentSearches : HomeEvent()
}