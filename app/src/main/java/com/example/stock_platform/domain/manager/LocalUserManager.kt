package com.example.stock_platform.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveUserApi(api: String)
    fun readUserApi(): Flow<String>
    suspend fun removeUserApi()
}