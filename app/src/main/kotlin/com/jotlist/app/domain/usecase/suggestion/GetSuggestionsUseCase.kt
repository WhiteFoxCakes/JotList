package com.jotlist.app.domain.usecase.suggestion

import com.jotlist.app.domain.model.Suggestion
import com.jotlist.app.domain.repository.SuggestionRepository
import javax.inject.Inject

/**
 * Use case to retrieve word suggestions based on a query.
 * Suggestions appear after 3 characters, showing max 3 results ordered by frequency.
 */
class GetSuggestionsUseCase @Inject constructor(
    private val repository: SuggestionRepository,
) {
    companion object {
        /** Minimum characters required before showing suggestions. */
        const val MIN_QUERY_LENGTH = 3

        /** Maximum number of suggestions to return. */
        const val MAX_RESULTS = 3
    }

    /**
     * Retrieves suggestions matching the query.
     *
     * @param query The search query.
     * @return List of matching suggestions, empty if query is less than 3 characters.
     */
    suspend operator fun invoke(query: String): List<Suggestion> {
        if (query.length < MIN_QUERY_LENGTH) {
            return emptyList()
        }

        return repository.getSuggestions(query.trim(), MAX_RESULTS)
    }
}
