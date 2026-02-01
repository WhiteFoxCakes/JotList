package com.jotlist.app.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jotlist.app.ui.components.ConfirmationDialog
import com.jotlist.app.ui.components.EmptyState
import com.jotlist.app.ui.components.ListCard
import com.jotlist.app.ui.theme.ElectricBlue
import com.jotlist.app.ui.theme.ScreenMargin
import com.jotlist.app.ui.theme.SoftRed
import com.jotlist.app.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToList: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToList -> onNavigateToList(event.listId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JotList",
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.height(64.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onCreateList,
                containerColor = ElectricBlue,
                shape = RoundedCornerShape(Spacing.md)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Create new list",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = ElectricBlue
                    )
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "An error occurred",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SoftRed
                    )
                }
            }

            uiState.lists.isEmpty() -> {
                EmptyState(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(ScreenMargin),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    verticalItemSpacing = Spacing.md
                ) {
                    items(
                        items = uiState.lists,
                        key = { it.list.id }
                    ) { listWithCount ->
                        ListCard(
                            list = listWithCount.list,
                            itemCount = listWithCount.itemCount,
                            onClick = { viewModel.onListClick(listWithCount.list.id) }
                        )
                    }
                }
            }
        }
    }

    uiState.listToDelete?.let { list ->
        ConfirmationDialog(
            title = "Delete List",
            message = "Are you sure you want to delete '${list.name}'? This action cannot be undone.",
            confirmText = "Delete",
            cancelText = "Cancel",
            isDestructive = true,
            onConfirm = viewModel::onDeleteListConfirm,
            onDismiss = viewModel::onDeleteListCancel
        )
    }
}
