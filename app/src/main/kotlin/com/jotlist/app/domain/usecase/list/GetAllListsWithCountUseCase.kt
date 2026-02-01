package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.model.ShoppingListWithCount
import com.jotlist.app.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all shopping lists with their item counts.
 * Lists are ordered by most recently updated.
 */
class GetAllListsWithCountUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    operator fun invoke(): Flow<List<ShoppingListWithCount>> {
        return repository.getAllListsWithItemCount()
    }
}
