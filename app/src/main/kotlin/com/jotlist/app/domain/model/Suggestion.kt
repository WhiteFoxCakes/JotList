package com.jotlist.app.domain.model

import com.jotlist.app.data.local.database.entity.SuggestionEntity

/**
 * Domain model representing a word suggestion based on previous entries.
 */
data class Suggestion(
    val id: Long = 0,
    val text: String,
    val usageCount: Int = 1,
    val lastUsedAt: Long,
    val createdAt: Long,
)

/**
 * Converts a SuggestionEntity to a Suggestion domain model.
 */
fun SuggestionEntity.toDomain(): Suggestion {
    return Suggestion(
        id = id,
        text = text,
        usageCount = usageCount,
        lastUsedAt = lastUsedAt,
        createdAt = createdAt,
    )
}

/**
 * Converts a Suggestion domain model to a SuggestionEntity.
 */
fun Suggestion.toEntity(): SuggestionEntity {
    return SuggestionEntity(
        id = id,
        text = text,
        usageCount = usageCount,
        lastUsedAt = lastUsedAt,
        createdAt = createdAt,
    )
}
