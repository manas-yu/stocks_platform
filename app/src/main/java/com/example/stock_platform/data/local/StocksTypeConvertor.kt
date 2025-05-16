package com.example.stock_platform.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.stock_platform.domain.model.gainers_losers.TopGainer
import com.example.stock_platform.domain.model.gainers_losers.TopLoser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class StocksTypeConvertor {

    private val gson = Gson()

    @TypeConverter
    fun fromTopGainersList(value: List<TopGainer>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTopGainersList(value: String): List<TopGainer> {
        val type = object : TypeToken<List<TopGainer>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTopLosersList(value: List<TopLoser>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTopLosersList(value: String): List<TopLoser> {
        val type = object : TypeToken<List<TopLoser>>() {}.type
        return gson.fromJson(value, type)
    }
}
