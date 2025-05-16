package com.example.stock_platform.domain.model.error_model

sealed class ErrorModel<out T> {
    data class Success<T>(val data: T) : ErrorModel<T>()
    data class Error(val exception: Throwable) : ErrorModel<Nothing>()
    object Loading : ErrorModel<Nothing>()
}
