package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.repository.ListItemRepository
import javax.inject.Inject

/**
 * Use case to delete an item from a shopping list.
 */
class DeleteItemUseCase @Inject constructor(
    private val repository: ListItemRepository,
) {
    /**
     * Deletes the item with the given ID.
     *
     * @param id The ID of the item to delete.
     */
    suspend operator fun invoke(id: Long) {
        repository.deleteItem(id)
    }
}
