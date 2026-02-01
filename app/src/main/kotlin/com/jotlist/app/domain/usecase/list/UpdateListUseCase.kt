package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.repository.ShoppingListRepository
import javax.inject.Inject

/**
 * Use case to update a shopping list's name.
 * Automatically updates the updatedAt timestamp.
 */
class UpdateListUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
) {
    /**
     * Updates the shopping list with a new name.
     *
     * @param list The shopping list to update (with the new name).
     */
    suspend operator fun invoke(list: ShoppingList) {
        val updatedList = list.copy(
            updatedAt = System.currentTimeMillis(),
        )
        repository.updateList(updatedList)
    }
}
