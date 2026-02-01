package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve a single shopping list by its ID.
 */
class GetListByIdUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
) {
    /**
     * Returns a Flow emitting the shopping list with the given ID, or null if not found.
     *
     * @param id The ID of the shopping list to retrieve.
     */
    operator fun invoke(id: Long): Flow<ShoppingList?> {
        return repository.getListById(id)
    }
}
