package com.jotlist.app.domain.repository

import com.jotlist.app.domain.model.Suggestion

/**
 * Repository interface for suggestion operations.
 * Implementations handle suggestion storage and retrieval based on usage frequency.
 */
interface SuggestionRepository {

    /**
     * Retrieves suggestions matching the query, ordered by usage frequency.
     *
     * @param query The search query (minimum 3 characters recommended).
     * @param limit Maximum number of suggestions to return (default: 3).
     */
    suspend fun getSuggestions(query: String, limit: Int = 3): List<Suggestion>

    /**
     * Adds or updates a suggestion based on the item text.
     * If the suggestion already exists, increments its usage count.
     * Otherwise, creates a new suggestion.
     *
     * @param text The item text to add as a suggestion.
     */
    suspend fun addOrUpdateSuggestion(text: String)
}
