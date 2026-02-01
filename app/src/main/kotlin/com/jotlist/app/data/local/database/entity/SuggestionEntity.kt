package com.jotlist.app.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a word suggestion based on previous entries.
 * Suggestions are ordered by usage frequency to show most commonly used items first.
 */
@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "usage_count")
    val usageCount: Int = 1,

    @ColumnInfo(name = "last_used_at")
    val lastUsedAt: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
