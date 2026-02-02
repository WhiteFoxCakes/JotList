package com.jotlist.app.ui.screens.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.model.Suggestion
import com.jotlist.app.domain.usecase.item.AddItemUseCase
import com.jotlist.app.domain.usecase.item.CheckItemUseCase
import com.jotlist.app.domain.usecase.item.DeleteItemUseCase
import com.jotlist.app.domain.usecase.item.GetItemsByListIdUseCase
import com.jotlist.app.domain.usecase.item.UncheckItemUseCase
import com.jotlist.app.domain.usecase.item.UpdateItemUseCase
import com.jotlist.app.domain.usecase.list.DeleteListUseCase
import com.jotlist.app.domain.usecase.list.GetListByIdUseCase
import com.jotlist.app.domain.usecase.list.UpdateListUseCase
import com.jotlist.app.domain.usecase.suggestion.GetSuggestionsUseCase
import com.jotlist.app.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the List Detail screen.
 */
data class ListDetailUiState(
    val currentList: ShoppingList? = null,
    val items: List<ListItem> = emptyList(),
    val inputText: String = "",
    val suggestions: List<Suggestion> = emptyList(),
    val showSuggestions: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val itemToDelete: ListItem? = null,
    val itemToEdit: ListItem? = null,
    val showDeleteListDialog: Boolean = false
)

/**
 * Navigation events for single-shot actions.
 */
sealed class ListDetailNavigationEvent {
    data object NavigateBack : ListDetailNavigationEvent()
}

/**
 * ViewModel for the List Detail screen managing shopping list items.
 */
@HiltViewModel
class ListDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getListByIdUseCase: GetListByIdUseCase,
    private val getItemsByListIdUseCase: GetItemsByListIdUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val checkItemUseCase: CheckItemUseCase,
    private val uncheckItemUseCase: UncheckItemUseCase,
    private val getSuggestionsUseCase: GetSuggestionsUseCase,
    private val updateListUseCase: UpdateListUseCase,
    private val deleteListUseCase: DeleteListUseCase
) : ViewModel() {

    private val listId: Long = savedStateHandle.get<Long>(Screen.LIST_ID_ARG) ?: 0L

    private val _uiState = MutableStateFlow(ListDetailUiState())
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<ListDetailNavigationEvent>(Channel.BUFFERED)
    val navigationEvent: Flow<ListDetailNavigationEvent> = _navigationEvent.receiveAsFlow()

    private var suggestionJob: Job? = null

    init {
        loadList()
        loadItems()
    }

    private fun loadList() {
        viewModelScope.launch {
            getListByIdUseCase(listId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load list"
                        )
                    }
                }
                .collect { list ->
                    _uiState.update {
                        it.copy(
                            currentList = list,
                            isLoading = false,
                            error = if (list == null) "List not found" else null
                        )
                    }
                }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            getItemsByListIdUseCase(listId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(error = exception.message ?: "Failed to load items")
                    }
                }
                .collect { items ->
                    _uiState.update { it.copy(items = items) }
                }
        }
    }

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }

        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            delay(200) // Debounce
            if (_uiState.value.inputText == text) {
                try {
                    val suggestions = getSuggestionsUseCase(text)
                    _uiState.update {
                        it.copy(
                            suggestions = suggestions,
                            showSuggestions = suggestions.isNotEmpty()
                        )
                    }
                } catch (exception: Exception) {
                    // Fail silently for suggestions
                }
            }
        }
    }

    fun onAddItem() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                addItemUseCase(listId, text)
                _uiState.update {
                    it.copy(
                        inputText = "",
                        suggestions = emptyList(),
                        showSuggestions = false
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(error = exception.message ?: "Failed to add item")
                }
            }
        }
    }

    fun onSuggestionClick(suggestion: Suggestion) {
        _uiState.update {
            it.copy(
                inputText = suggestion.text,
                suggestions = emptyList(),
                showSuggestions = false
            )
        }
    }

    fun onItemCheckToggle(item: ListItem) {
        viewModelScope.launch {
            try {
                if (item.isChecked) {
                    uncheckItemUseCase(item.id)
                } else {
                    checkItemUseCase(item.id)
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(error = exception.message ?: "Failed to update item")
                }
            }
        }
    }

    fun onEditItemRequest(item: ListItem) {
        _uiState.update { it.copy(itemToEdit = item) }
    }

    fun onEditItemConfirm(newText: String) {
        val item = _uiState.value.itemToEdit ?: return

        viewModelScope.launch {
            try {
                updateItemUseCase(item.id, newText)
                _uiState.update { it.copy(itemToEdit = null) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        itemToEdit = null,
                        error = exception.message ?: "Failed to update item"
                    )
                }
            }
        }
    }

    fun onEditItemCancel() {
        _uiState.update { it.copy(itemToEdit = null) }
    }

    fun onDeleteItemRequest(item: ListItem) {
        _uiState.update { it.copy(itemToDelete = item) }
    }

    fun onDeleteItemConfirm() {
        val item = _uiState.value.itemToDelete ?: return

        viewModelScope.launch {
            try {
                deleteItemUseCase(item.id)
                _uiState.update { it.copy(itemToDelete = null) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        itemToDelete = null,
                        error = exception.message ?: "Failed to delete item"
                    )
                }
            }
        }
    }

    fun onDeleteItemCancel() {
        _uiState.update { it.copy(itemToDelete = null) }
    }

    fun onDeleteListRequest() {
        _uiState.update { it.copy(showDeleteListDialog = true) }
    }

    fun onDeleteListConfirm() {
        viewModelScope.launch {
            try {
                deleteListUseCase(listId)
                _navigationEvent.send(ListDetailNavigationEvent.NavigateBack)
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        showDeleteListDialog = false,
                        error = exception.message ?: "Failed to delete list"
                    )
                }
            }
        }
    }

    fun onDeleteListCancel() {
        _uiState.update { it.copy(showDeleteListDialog = false) }
    }

    fun onBackPressed() {
        viewModelScope.launch {
            _navigationEvent.send(ListDetailNavigationEvent.NavigateBack)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
