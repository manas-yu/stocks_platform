package com.example.stock_platform.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stock_platform.data.remote.dto.GainersLosersResponse

@Dao
interface GainersLosersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(response: GainersLosersResponse)

    // Get the most recent entry (only one entry is expected)
    @Query("SELECT * FROM gainers_losers ORDER BY timeStamp DESC LIMIT 1")
    suspend fun getMostRecent(): GainersLosersResponse?
}
