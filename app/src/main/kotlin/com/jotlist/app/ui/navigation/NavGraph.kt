package com.jotlist.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Main navigation graph for JotList.
 * Contains routes for Home and ListDetail screens.
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            // TODO: Replace with HomeScreen
            PlaceholderScreen(screenName = "Home")
        }

        composable(
            route = Screen.ListDetail.route,
            arguments = listOf(
                navArgument(Screen.LIST_ID_ARG) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong(Screen.LIST_ID_ARG) ?: 0L
            // TODO: Replace with ListDetailScreen
            PlaceholderScreen(screenName = "List Detail (ID: $listId)")
        }
    }
}

/**
 * Temporary placeholder screen for development.
 */
@Composable
private fun PlaceholderScreen(screenName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = screenName,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
