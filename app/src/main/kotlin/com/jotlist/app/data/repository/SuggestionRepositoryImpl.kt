package com.jotlist.app.data.repository

import com.jotlist.app.data.local.database.dao.SuggestionDao
import com.jotlist.app.data.local.database.entity.SuggestionEntity
import com.jotlist.app.domain.model.Suggestion
import com.jotlist.app.domain.model.toDomain
import com.jotlist.app.domain.repository.SuggestionRepository
import javax.inject.Inject

/**
 * Implementation of [SuggestionRepository] using Room database.
 */
class SuggestionRepositoryImpl @Inject constructor(
    private val dao: SuggestionDao,
) : SuggestionRepository {

    override suspend fun getSuggestions(query: String, limit: Int): List<Suggestion> {
        return dao.getSuggestions(query, limit).map { it.toDomain() }
    }

    override suspend fun addOrUpdateSuggestion(text: String) {
        val currentTime = System.currentTimeMillis()
        val existing = dao.getSuggestionByText(text)

        if (existing != null) {
            dao.incrementUsageCount(existing.id, currentTime)
        } else {
            val newSuggestion = SuggestionEntity(
                text = text,
                usageCount = 1,
                lastUsedAt = currentTime,
                createdAt = currentTime,
            )
            dao.insertSuggestion(newSuggestion)
        }
    }
}
