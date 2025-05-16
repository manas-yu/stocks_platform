package com.example.stock_platform.presentation.home

sealed class HomeEvent {
     object LoadTopGainers : HomeEvent()
     object LoadTopLosers : HomeEvent()
     object RefreshData : HomeEvent()
}