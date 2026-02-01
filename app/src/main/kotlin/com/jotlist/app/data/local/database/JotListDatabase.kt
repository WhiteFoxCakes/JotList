package com.jotlist.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jotlist.app.data.local.database.dao.ListItemDao
import com.jotlist.app.data.local.database.dao.ShoppingListDao
import com.jotlist.app.data.local.database.dao.SuggestionDao
import com.jotlist.app.data.local.database.entity.ListItemEntity
import com.jotlist.app.data.local.database.entity.ShoppingListEntity
import com.jotlist.app.data.local.database.entity.SuggestionEntity

/**
 * Room database for the JotList application.
 *
 * Contains tables for shopping lists, list items, and word suggestions.
 */
@Database(
    entities = [
        ShoppingListEntity::class,
        ListItemEntity::class,
        SuggestionEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class JotListDatabase : RoomDatabase() {

    /**
     * Provides access to shopping list operations.
     */
    abstract fun shoppingListDao(): ShoppingListDao

    /**
     * Provides access to list item operations.
     */
    abstract fun listItemDao(): ListItemDao

    /**
     * Provides access to suggestion operations.
     */
    abstract fun suggestionDao(): SuggestionDao

    companion object {
        const val DATABASE_NAME = "jotlist_database"
    }
}
