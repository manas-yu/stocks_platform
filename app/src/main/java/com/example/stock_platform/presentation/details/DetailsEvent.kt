package com.example.stock_platform.presentation.details

sealed class DetailsEvent {
    object LoadStockDetails : DetailsEvent()
}