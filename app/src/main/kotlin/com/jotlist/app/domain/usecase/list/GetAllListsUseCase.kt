package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve all shopping lists ordered by most recently updated.
 */
class GetAllListsUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
) {
    /**
     * Returns a Flow of all shopping lists.
     */
    operator fun invoke(): Flow<List<ShoppingList>> {
        return repository.getAllLists()
    }
}
