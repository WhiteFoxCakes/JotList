package com.jotlist.app.data.repository

import com.jotlist.app.data.local.database.dao.ShoppingListDao
import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.domain.model.ShoppingListWithCount
import com.jotlist.app.domain.model.toDomain
import com.jotlist.app.domain.model.toEntity
import com.jotlist.app.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ShoppingListRepository] using Room database.
 */
class ShoppingListRepositoryImpl @Inject constructor(
    private val dao: ShoppingListDao,
) : ShoppingListRepository {

    override fun getAllLists(): Flow<List<ShoppingList>> {
        return dao.getAllLists().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllListsWithItemCount(): Flow<List<ShoppingListWithCount>> {
        return dao.getAllListsWithItemCount().map { entities ->
            entities.map { entity ->
                ShoppingListWithCount(
                    list = entity.list.toDomain(),
                    itemCount = entity.itemCount
                )
            }
        }
    }

    override fun getListById(id: Long): Flow<ShoppingList?> {
        return dao.getListById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun createList(list: ShoppingList): Long {
        return dao.insertList(list.toEntity())
    }

    override suspend fun updateList(list: ShoppingList) {
        dao.updateList(list.toEntity())
    }

    override suspend fun deleteList(id: Long) {
        dao.deleteListById(id)
    }
}
