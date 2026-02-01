package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.repository.ShoppingListRepository
import javax.inject.Inject

/**
 * Use case to delete a shopping list.
 * Associated items are deleted automatically via CASCADE in the database.
 */
class DeleteListUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
) {
    /**
     * Deletes the shopping list with the given ID.
     *
     * @param id The ID of the list to delete.
     */
    suspend operator fun invoke(id: Long) {
        repository.deleteList(id)
    }
}
