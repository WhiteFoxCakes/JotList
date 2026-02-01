package com.jotlist.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jotlist.app.data.local.database.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for shopping list operations.
 */
@Dao
interface ShoppingListDao {

    /**
     * Retrieves all shopping lists ordered by most recently updated.
     */
    @Query("SELECT * FROM shopping_lists ORDER BY updated_at DESC")
    fun getAllLists(): Flow<List<ShoppingListEntity>>

    /**
     * Retrieves a single shopping list by its ID.
     */
    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    fun getListById(id: Long): Flow<ShoppingListEntity?>

    /**
     * Inserts a new shopping list or replaces an existing one.
     * @return The row ID of the inserted list.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity): Long

    /**
     * Updates an existing shopping list.
     */
    @Update
    suspend fun updateList(list: ShoppingListEntity)

    /**
     * Deletes a shopping list.
     */
    @Delete
    suspend fun deleteList(list: ShoppingListEntity)

    /**
     * Deletes a shopping list by its ID.
     */
    @Query("DELETE FROM shopping_lists WHERE id = :id")
    suspend fun deleteListById(id: Long)
}
