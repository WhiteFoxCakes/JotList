package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.repository.ListItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all items in a specific shopping list.
 * Items are automatically ordered by the DAO query:
 * - Unchecked items first (sorted by position)
 * - Checked items last (maintaining relative position)
 */
class GetItemsByListIdUseCase @Inject constructor(
    private val repository: ListItemRepository
) {
    operator fun invoke(listId: Long): Flow<List<ListItem>> {
        return repository.getItemsByListId(listId)
    }
}
