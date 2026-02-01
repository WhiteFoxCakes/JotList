package com.jotlist.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jotlist.app.data.local.database.entity.SuggestionEntity

/**
 * Data Access Object for suggestion operations.
 * Suggestions are based on previously entered items and ordered by usage frequency.
 */
@Dao
interface SuggestionDao {

    /**
     * Retrieves suggestions matching the query, ordered by usage frequency.
     * Matching is case-insensitive and searches for the query anywhere in the text.
     *
     * @param query The search query (minimum 3 characters recommended).
     * @param limit Maximum number of suggestions to return (default: 3).
     */
    @Query(
        """
        SELECT * FROM suggestions
        WHERE LOWER(text) LIKE '%' || LOWER(:query) || '%'
        ORDER BY usage_count DESC
        LIMIT :limit
        """,
    )
    suspend fun getSuggestions(query: String, limit: Int = 3): List<SuggestionEntity>

    /**
     * Finds a suggestion by its exact text (case-insensitive).
     * Used to check if a suggestion already exists before inserting.
     */
    @Query("SELECT * FROM suggestions WHERE LOWER(text) = LOWER(:text) LIMIT 1")
    suspend fun getSuggestionByText(text: String): SuggestionEntity?

    /**
     * Inserts a new suggestion or replaces an existing one.
     * @return The row ID of the inserted suggestion.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SuggestionEntity): Long

    /**
     * Updates an existing suggestion.
     */
    @Update
    suspend fun updateSuggestion(suggestion: SuggestionEntity)

    /**
     * Increments the usage count for a suggestion and updates its last used timestamp.
     */
    @Query("UPDATE suggestions SET usage_count = usage_count + 1, last_used_at = :timestamp WHERE id = :id")
    suspend fun incrementUsageCount(id: Long, timestamp: Long)
}
