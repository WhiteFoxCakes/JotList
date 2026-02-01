package com.jotlist.app.ui.navigation

/**
 * Sealed class representing navigation destinations in the app.
 */
sealed class Screen(val route: String) {
    /**
     * Home screen showing all shopping lists.
     */
    data object Home : Screen("home")

    /**
     * List detail screen showing items in a specific list.
     * @param listId The ID of the list to display
     */
    data object ListDetail : Screen("list/{listId}") {
        fun createRoute(listId: Long): String = "list/$listId"
    }

    companion object {
        const val LIST_ID_ARG = "listId"
    }
}
