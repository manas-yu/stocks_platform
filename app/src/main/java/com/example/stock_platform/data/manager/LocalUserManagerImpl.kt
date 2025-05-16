package com.example.stock_platform.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.stock_platform.domain.manager.LocalUserManager
import com.example.stock_platform.util.Constants
import com.example.stock_platform.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalUserManagerImpl(private val context: Context) : LocalUserManager {
    override suspend fun saveUserApi(api: String) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.USER_API] = api
        }
    }

    override fun readUserApi(): Flow<String> {
        return context.dataStore.data.map {
            it[PreferencesKeys.USER_API] ?: ""
        }
    }

    override suspend fun removeUserApi() {
        context.dataStore.edit { settings ->
            settings.remove(PreferencesKeys.USER_API)
        }
    }


}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS)

private object PreferencesKeys {
    val USER_API = stringPreferencesKey(name = Constants.API_KEY)
}