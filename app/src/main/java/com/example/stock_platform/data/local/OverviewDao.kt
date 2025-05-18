package com.example.stock_platform.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stock_platform.data.remote.dto.OverviewResponse

@Dao
interface OverviewDao {

    // Insert or update an OverviewResponse
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOverview(overview: OverviewResponse)

    // Get a specific OverviewResponse by symbol
    @Query("SELECT * FROM company_overview WHERE symbol = :symbol LIMIT 1")
    suspend fun getOverviewBySymbol(symbol: String): OverviewResponse?

    // Delete entries older than one hour
    @Query("DELETE FROM company_overview WHERE timeStamp < :expiryTime")
    suspend fun deleteEntriesOlderThan(expiryTime: Long)
}
