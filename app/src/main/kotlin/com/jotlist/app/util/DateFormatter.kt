package com.jotlist.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Utility class for formatting dates.
 * Uses Hilt injection to allow for easier testing.
 */
class DateFormatter @Inject constructor() {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

    /**
     * Formats a timestamp to a human-readable date string.
     *
     * @param timestamp The timestamp in milliseconds.
     * @return Formatted date string (e.g., "Feb 1, 2026").
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }
}
