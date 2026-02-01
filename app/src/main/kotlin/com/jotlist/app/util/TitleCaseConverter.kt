package com.jotlist.app.util

/**
 * Utility object for converting text to title case.
 * Each word's first letter is capitalized, remaining letters are lowercase.
 */
object TitleCaseConverter {

    /**
     * Converts the input string to title case.
     *
     * @param input The string to convert.
     * @return The title-cased string.
     *
     * Examples:
     * - "milk" -> "Milk"
     * - "ORANGE JUICE" -> "Orange Juice"
     * - "bread and butter" -> "Bread And Butter"
     * - "2% milk" -> "2% Milk"
     */
    fun convert(input: String): String {
        if (input.isBlank()) return input.trim()

        return input.trim()
            .lowercase()
            .split(" ")
            .filter { it.isNotEmpty() }
            .joinToString(" ") { word ->
                word.replaceFirstChar { char ->
                    if (char.isLetter()) char.titlecase() else char.toString()
                }
            }
    }
}
