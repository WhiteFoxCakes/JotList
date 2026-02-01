package com.jotlist.app.domain.usecase.item

import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.repository.ListItemRepository
import com.jotlist.app.util.TitleCaseConverter
import javax.inject.Inject

/**
 * Use case to update an existing item's text.
 * Automatically applies title case formatting.
 */
class UpdateItemUseCase @Inject constructor(
    private val repository: ListItemRepository,
) {
    /**
     * Updates the item with new text.
     *
     * @param item The item to update with the new text value.
     */
    suspend operator fun invoke(item: ListItem) {
        val titleCasedText = TitleCaseConverter.convert(item.text)
        val updatedItem = item.copy(text = titleCasedText)
        repository.updateItem(updatedItem)
    }

    /**
     * Updates the item with new text by ID.
     *
     * @param id The ID of the item to update.
     * @param newText The new text for the item.
     * @throws IllegalArgumentException if item is not found or text is blank.
     */
    suspend operator fun invoke(id: Long, newText: String) {
        if (newText.isBlank()) {
            throw IllegalArgumentException("Item text cannot be blank")
        }

        val existingItem = repository.getItemById(id)
            ?: throw IllegalArgumentException("Item with id $id not found")

        val titleCasedText = TitleCaseConverter.convert(newText)
        val updatedItem = existingItem.copy(text = titleCasedText)
        repository.updateItem(updatedItem)
    }
}
