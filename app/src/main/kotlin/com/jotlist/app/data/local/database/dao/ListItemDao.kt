package com.jotlist.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jotlist.app.data.local.database.entity.ListItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for list item operations.
 */
@Dao
interface ListItemDao {

    /**
     * Retrieves all items for a specific list.
     * Items are ordered with unchecked items first (by position), then checked items.
     */
    @Query("SELECT * FROM list_items WHERE list_id = :listId ORDER BY is_checked ASC, position ASC")
    fun getItemsByListId(listId: Long): Flow<List<ListItemEntity>>

    /**
     * Retrieves a single item by its ID.
     */
    @Query("SELECT * FROM list_items WHERE id = :id")
    suspend fun getItemById(id: Long): ListItemEntity?

    /**
     * Returns the total count of items in a list.
     */
    @Query("SELECT COUNT(*) FROM list_items WHERE list_id = :listId")
    fun getItemCount(listId: Long): Flow<Int>

    /**
     * Returns the count of unchecked items in a list.
     */
    @Query("SELECT COUNT(*) FROM list_items WHERE list_id = :listId AND is_checked = 0")
    fun getUncheckedItemCount(listId: Long): Flow<Int>

    /**
     * Inserts a new item or replaces an existing one.
     * @return The row ID of the inserted item.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ListItemEntity): Long

    /**
     * Updates an existing item.
     */
    @Update
    suspend fun updateItem(item: ListItemEntity)

    /**
     * Updates multiple items at once (used for reordering).
     */
    @Update
    suspend fun updateItems(items: List<ListItemEntity>)

    /**
     * Deletes an item.
     */
    @Delete
    suspend fun deleteItem(item: ListItemEntity)

    /**
     * Deletes an item by its ID.
     */
    @Query("DELETE FROM list_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    /**
     * Gets the next available position for a new item in a list.
     * Returns 0 if the list is empty, otherwise MAX(position) + 1.
     */
    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM list_items WHERE list_id = :listId")
    suspend fun getNextPosition(listId: Long): Int
}
