package com.example.stock_platform.domain.usecases.app_entry

data class AppEntryUseCases(
    val readUserApi: ReadUserApi,
    val saveUserApi: SaveUserApi,
    val removeUserApi: RemoveUserApi
)
