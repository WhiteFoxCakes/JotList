package com.jotlist.app.data.repository

import com.jotlist.app.data.local.database.dao.ListItemDao
import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.domain.model.toDomain
import com.jotlist.app.domain.model.toEntity
import com.jotlist.app.domain.repository.ListItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ListItemRepository] using Room database.
 */
class ListItemRepositoryImpl @Inject constructor(
    private val dao: ListItemDao,
) : ListItemRepository {

    override fun getItemsByListId(listId: Long): Flow<List<ListItem>> {
        return dao.getItemsByListId(listId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getItemById(id: Long): ListItem? {
        return dao.getItemById(id)?.toDomain()
    }

    override fun getItemCount(listId: Long): Flow<Int> {
        return dao.getItemCount(listId)
    }

    override fun getUncheckedItemCount(listId: Long): Flow<Int> {
        return dao.getUncheckedItemCount(listId)
    }

    override suspend fun addItem(item: ListItem): Long {
        return dao.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: ListItem) {
        dao.updateItem(item.toEntity())
    }

    override suspend fun updateItems(items: List<ListItem>) {
        dao.updateItems(items.map { it.toEntity() })
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteItemById(id)
    }

    override suspend fun getNextPosition(listId: Long): Int {
        return dao.getNextPosition(listId)
    }
}
