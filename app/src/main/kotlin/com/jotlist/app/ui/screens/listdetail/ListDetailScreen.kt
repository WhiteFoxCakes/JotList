package com.jotlist.app.ui.screens.listdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jotlist.app.ui.components.ConfirmationDialog
import com.jotlist.app.ui.components.EditItemDialog
import com.jotlist.app.ui.components.EmptyState
import com.jotlist.app.ui.components.InputFieldWithSuggestions
import com.jotlist.app.ui.components.ListDetailTopBar
import com.jotlist.app.ui.components.ListItemRow
import com.jotlist.app.ui.theme.ElectricBlue
import com.jotlist.app.ui.theme.ScreenMargin
import com.jotlist.app.ui.theme.SoftRed
import com.jotlist.app.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: ListDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                ListDetailNavigationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            ListDetailTopBar(
                listName = uiState.currentList?.name ?: "",
                onBackClick = viewModel::onBackPressed,
                onRenameClick = { /* TODO: Implement rename in Phase 7 */ },
                onDeleteClick = viewModel::onDeleteListRequest
            )
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

            uiState.error != null && uiState.currentList == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.md)
                    ) {
                        Text(
                            text = uiState.error ?: "An error occurred",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SoftRed
                        )
                    }
                }
            }

            uiState.currentList == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "List not found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SoftRed
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    InputFieldWithSuggestions(
                        value = uiState.inputText,
                        onValueChange = viewModel::onInputTextChanged,
                        onSubmit = viewModel::onAddItem,
                        suggestions = uiState.suggestions,
                        showSuggestions = uiState.showSuggestions,
                        onSuggestionClick = viewModel::onSuggestionClick,
                        modifier = Modifier.padding(
                            horizontal = ScreenMargin,
                            vertical = Spacing.md
                        )
                    )

                    if (uiState.items.isEmpty()) {
                        EmptyState(
                            message = "No items yet"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = ScreenMargin,
                                end = ScreenMargin,
                                bottom = Spacing.md
                            ),
                            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                        ) {
                            items(
                                items = uiState.items,
                                key = { it.id }
                            ) { item ->
                                ListItemRow(
                                    item = item,
                                    onCheckToggle = viewModel::onItemCheckToggle,
                                    onEdit = viewModel::onEditItemRequest,
                                    onDelete = viewModel::onDeleteItemRequest
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete item confirmation dialog
    uiState.itemToDelete?.let { item ->
        ConfirmationDialog(
            title = "Delete Item",
            message = "Are you sure you want to delete '${item.text}'?",
            confirmText = "Delete",
            cancelText = "Cancel",
            isDestructive = true,
            onConfirm = viewModel::onDeleteItemConfirm,
            onDismiss = viewModel::onDeleteItemCancel
        )
    }

    // Edit item dialog
    uiState.itemToEdit?.let { item ->
        EditItemDialog(
            currentText = item.text,
            onSave = viewModel::onEditItemConfirm,
            onDismiss = viewModel::onEditItemCancel
        )
    }

    // Delete list confirmation dialog
    if (uiState.showDeleteListDialog) {
        ConfirmationDialog(
            title = "Delete List",
            message = "Are you sure you want to delete '${uiState.currentList?.name}'? This will delete all items.",
            confirmText = "Delete",
            cancelText = "Cancel",
            isDestructive = true,
            onConfirm = viewModel::onDeleteListConfirm,
            onDismiss = viewModel::onDeleteListCancel
        )
    }
}
