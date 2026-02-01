package com.jotlist.app.domain.repository

import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.model.ShoppingListWithCount
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for shopping list operations.
 * Implementations handle data access and provide a clean API for the domain layer.
 */
interface ShoppingListRepository {

    /**
     * Retrieves all shopping lists ordered by most recently updated.
     */
    fun getAllLists(): Flow<List<ShoppingList>>

    /**
     * Retrieves all shopping lists with their item counts, ordered by most recently updated.
     */
    fun getAllListsWithItemCount(): Flow<List<ShoppingListWithCount>>

    /**
     * Retrieves a single shopping list by its ID.
     */
    fun getListById(id: Long): Flow<ShoppingList?>

    /**
     * Creates a new shopping list.
     * @return The ID of the created list.
     */
    suspend fun createList(list: ShoppingList): Long

    /**
     * Updates an existing shopping list.
     */
    suspend fun updateList(list: ShoppingList)

    /**
     * Deletes a shopping list by its ID.
     * Associated items are deleted automatically via CASCADE.
     */
    suspend fun deleteList(id: Long)
}
