package com.example.stock_platform.domain.usecases.app_entry

import com.example.stock_platform.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadUserApi(private val localUserManager: LocalUserManager) {
    operator fun invoke(): Flow<String> {
        return localUserManager.readUserApi()
    }
}