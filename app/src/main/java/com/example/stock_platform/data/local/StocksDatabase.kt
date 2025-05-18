package com.example.stock_platform.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stock_platform.data.remote.dto.GainersLosersResponse
import com.example.stock_platform.data.remote.dto.OverviewResponse
import com.example.stock_platform.domain.model.search.BestMatch

@Database(
    entities = [GainersLosersResponse::class, BestMatch::class, OverviewResponse::class],
    version = 3
)
@TypeConverters(StocksTypeConvertor::class)
abstract class StocksDatabase: RoomDatabase()  {
    abstract val gainersLosersDao: GainersLosersDao
    abstract val searchDao: SearchDao
    abstract val overviewDao: OverviewDao
}