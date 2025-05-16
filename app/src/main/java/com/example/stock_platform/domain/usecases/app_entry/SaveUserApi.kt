package com.example.stock_platform.domain.usecases.app_entry

import com.example.stock_platform.domain.manager.LocalUserManager


class SaveUserApi(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke(api: String) {
        localUserManager.saveUserApi(api)
    }
}