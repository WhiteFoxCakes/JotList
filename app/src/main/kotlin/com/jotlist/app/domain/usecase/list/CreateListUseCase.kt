package com.jotlist.app.domain.usecase.list

import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.repository.ShoppingListRepository
import com.jotlist.app.util.DateFormatter
import javax.inject.Inject

/**
 * Use case to create a new shopping list.
 * If no name is provided or the name is blank, generates a default name
 * using the format "List (Feb 1, 2026)".
 */
class CreateListUseCase @Inject constructor(
    private val repository: ShoppingListRepository,
    private val dateFormatter: DateFormatter,
) {
    /**
     * Creates a new shopping list.
     *
     * @param name Optional name for the list. If null or blank, a default name is generated.
     * @return The ID of the created list.
     */
    suspend operator fun invoke(name: String? = null): Long {
        val currentTime = System.currentTimeMillis()

        val listName = if (name.isNullOrBlank()) {
            "List (${dateFormatter.formatDate(currentTime)})"
        } else {
            name.trim()
        }

        val list = ShoppingList(
            name = listName,
            createdAt = currentTime,
            updatedAt = currentTime,
        )

        return repository.createList(list)
    }
}
