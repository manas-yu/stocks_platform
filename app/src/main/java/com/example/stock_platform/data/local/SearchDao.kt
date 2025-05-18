package com.example.stock_platform.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stock_platform.domain.model.search.BestMatch
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(bestMatch: BestMatch)

    // Get only searches from the last 1 hour
    @Query("SELECT * FROM best_match WHERE timestamp >= :cutoffTime ORDER BY timestamp DESC")
    fun getRecentSearches(cutoffTime: Long): Flow<List<BestMatch>>

    // Delete searches older than cutoffTime
    @Query("DELETE FROM best_match WHERE timestamp < :cutoffTime")
    suspend fun deleteOlderThan(cutoffTime: Long)
}
