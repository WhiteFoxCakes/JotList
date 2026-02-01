package com.jotlist.app.domain.repository

import com.jotlist.app.domain.model.ListItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for list item operations.
 * Implementations handle data access and provide a clean API for the domain layer.
 */
interface ListItemRepository {

    /**
     * Retrieves all items for a specific list.
     * Items are ordered with unchecked items first (by position), then checked items.
     */
    fun getItemsByListId(listId: Long): Flow<List<ListItem>>

    /**
     * Retrieves a single item by its ID.
     */
    suspend fun getItemById(id: Long): ListItem?

    /**
     * Returns the total count of items in a list.
     */
    fun getItemCount(listId: Long): Flow<Int>

    /**
     * Returns the count of unchecked items in a list.
     */
    fun getUncheckedItemCount(listId: Long): Flow<Int>

    /**
     * Adds a new item to a list.
     * @return The ID of the created item.
     */
    suspend fun addItem(item: ListItem): Long

    /**
     * Updates an existing item.
     */
    suspend fun updateItem(item: ListItem)

    /**
     * Updates multiple items at once (used for reordering).
     */
    suspend fun updateItems(items: List<ListItem>)

    /**
     * Deletes an item by its ID.
     */
    suspend fun deleteItem(id: Long)

    /**
     * Gets the next available position for a new item in a list.
     */
    suspend fun getNextPosition(listId: Long): Int
}
