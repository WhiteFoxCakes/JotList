package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.repository.ListItemRepository
import javax.inject.Inject

/**
 * Use case to reorder items after drag-and-drop.
 * Only unchecked items can be reordered.
 */
class ReorderItemsUseCase @Inject constructor(
    private val repository: ListItemRepository,
) {
    /**
     * Updates the positions of items based on their new order.
     *
     * @param items The list of items in their new order.
     */
    suspend operator fun invoke(items: List<ListItem>) {
        val reorderedItems = items.mapIndexed { index, item ->
            item.copy(position = index)
        }
        repository.updateItems(reorderedItems)
    }
}
