package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.repository.ListItemRepository
import javax.inject.Inject

/**
 * Use case to uncheck an item and restore it to its original position.
 */
class UncheckItemUseCase @Inject constructor(
    private val repository: ListItemRepository,
) {
    /**
     * Unchecks the item with the given ID and restores its position.
     *
     * @param id The ID of the item to uncheck.
     * @throws IllegalArgumentException if item is not found.
     */
    suspend operator fun invoke(id: Long) {
        val item = repository.getItemById(id)
            ?: throw IllegalArgumentException("Item with id $id not found")

        if (!item.isChecked) {
            // Already unchecked, no action needed
            return
        }

        val restoredPosition = item.originalPosition ?: item.position

        val uncheckedItem = item.copy(
            isChecked = false,
            position = restoredPosition,
            originalPosition = null,
        )

        repository.updateItem(uncheckedItem)
    }
}
