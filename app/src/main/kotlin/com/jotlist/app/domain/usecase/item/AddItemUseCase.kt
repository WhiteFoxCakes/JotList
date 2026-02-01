package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.repository.ListItemRepository
import com.jotlist.app.domain.usecase.suggestion.AddSuggestionUseCase
import com.jotlist.app.util.TitleCaseConverter
import javax.inject.Inject

/**
 * Use case to add a new item to a shopping list.
 * Automatically applies title case formatting and adds the item to suggestions.
 */
class AddItemUseCase @Inject constructor(
    private val itemRepository: ListItemRepository,
    private val addSuggestionUseCase: AddSuggestionUseCase,
) {
    /**
     * Adds a new item to the specified list.
     *
     * @param listId The ID of the list to add the item to.
     * @param text The item text (will be converted to title case).
     * @return The ID of the created item.
     */
    suspend operator fun invoke(listId: Long, text: String): Long {
        if (text.isBlank()) {
            throw IllegalArgumentException("Item text cannot be blank")
        }

        val titleCasedText = TitleCaseConverter.convert(text)
        val position = itemRepository.getNextPosition(listId)
        val currentTime = System.currentTimeMillis()

        val item = ListItem(
            listId = listId,
            text = titleCasedText,
            isChecked = false,
            position = position,
            originalPosition = null,
            createdAt = currentTime,
        )

        val itemId = itemRepository.addItem(item)

        // Add to suggestions
        addSuggestionUseCase(titleCasedText)

        return itemId
    }
}
