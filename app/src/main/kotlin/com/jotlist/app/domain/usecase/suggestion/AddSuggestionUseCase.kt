package com.jotlist.app.domain.usecase.suggestion

import com.jotlist.app.domain.repository.SuggestionRepository
import com.jotlist.app.util.TitleCaseConverter
import javax.inject.Inject

/**
 * Use case to add or update a suggestion.
 * If the suggestion already exists (case-insensitive), increments its usage count.
 * Suggestions are stored in title case format.
 */
class AddSuggestionUseCase @Inject constructor(
    private val repository: SuggestionRepository,
) {
    /**
     * Adds or updates a suggestion based on the item text.
     *
     * @param text The item text to add as a suggestion.
     */
    suspend operator fun invoke(text: String) {
        if (text.isBlank()) return

        val titleCasedText = TitleCaseConverter.convert(text)
        repository.addOrUpdateSuggestion(titleCasedText)
    }
}
