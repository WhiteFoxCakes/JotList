package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.repository.ListItemRepository
import javax.inject.Inject

/**
 * Use case to check (mark as complete) an item.
 * Stores the current position as originalPosition to allow restoring later.
 */
class CheckItemUseCase @Inject constructor(
    private val repository: ListItemRepository,
) {
    /**
     * Checks the item with the given ID.
     *
     * @param id The ID of the item to check.
     * @throws IllegalArgumentException if item is not found.
     */
    suspend operator fun invoke(id: Long) {
        val item = repository.getItemById(id)
            ?: throw IllegalArgumentException("Item with id $id not found")

        if (item.isChecked) {
            // Already checked, no action needed
            return
        }

        val checkedItem = item.copy(
            isChecked = true,
            originalPosition = item.position,
        )

        repository.updateItem(checkedItem)
    }
}
