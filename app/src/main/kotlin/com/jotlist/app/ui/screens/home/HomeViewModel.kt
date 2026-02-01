package com.jotlist.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.model.ShoppingListWithCount
import com.jotlist.app.domain.usecase.list.CreateListUseCase
import com.jotlist.app.domain.usecase.list.DeleteListUseCase
import com.jotlist.app.domain.usecase.list.GetAllListsWithCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
 * UI state for the Home screen.
 */
data class HomeUiState(
    val lists: List<ShoppingListWithCount> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val listToDelete: ShoppingList? = null
)

/**
 * Navigation events for single-shot actions.
 */
sealed class NavigationEvent {
    data class NavigateToList(val listId: Long) : NavigationEvent()
}

/**
 * ViewModel for the Home screen managing shopping lists.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllListsWithCountUseCase: GetAllListsWithCountUseCase,
    private val createListUseCase: CreateListUseCase,
    private val deleteListUseCase: DeleteListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvent: Flow<NavigationEvent> = _navigationEvent.receiveAsFlow()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            getAllListsWithCountUseCase()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load lists"
                        )
                    }
                }
                .collect { lists ->
                    _uiState.update {
                        it.copy(
                            lists = lists,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onCreateList() {
        viewModelScope.launch {
            try {
                val listId = createListUseCase(name = null)
                _navigationEvent.send(NavigationEvent.NavigateToList(listId))
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(error = exception.message ?: "Failed to create list")
                }
            }
        }
    }

    fun onListClick(listId: Long) {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateToList(listId))
        }
    }

    fun onDeleteListRequest(list: ShoppingList) {
        _uiState.update { it.copy(listToDelete = list) }
    }

    fun onDeleteListConfirm() {
        val listToDelete = _uiState.value.listToDelete ?: return

        viewModelScope.launch {
            try {
                deleteListUseCase(listToDelete.id)
                _uiState.update { it.copy(listToDelete = null) }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        listToDelete = null,
                        error = exception.message ?: "Failed to delete list"
                    )
                }
            }
        }
    }

    fun onDeleteListCancel() {
        _uiState.update { it.copy(listToDelete = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
