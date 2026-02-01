package com.jotlist.app.domain.model

import com.jotlist.app.data.local.database.entity.ListItemEntity

/**
 * Domain model representing an item within a shopping list.
 *
 * @property originalPosition Stores the item's position before being checked,
 *           allowing it to return to the exact same position when unchecked.
 */
data class ListItem(
    val id: Long = 0,
    val listId: Long,
    val text: String,
    val isChecked: Boolean = false,
    val position: Int,
    val originalPosition: Int? = null,
    val createdAt: Long,
)

/**
 * Converts a ListItemEntity to a ListItem domain model.
 */
fun ListItemEntity.toDomain(): ListItem {
    return ListItem(
        id = id,
        listId = listId,
        text = text,
        isChecked = isChecked,
        position = position,
        originalPosition = originalPosition,
        createdAt = createdAt,
    )
}

/**
 * Converts a ListItem domain model to a ListItemEntity.
 */
fun ListItem.toEntity(): ListItemEntity {
    return ListItemEntity(
        id = id,
        listId = listId,
        text = text,
        isChecked = isChecked,
        position = position,
        originalPosition = originalPosition,
        createdAt = createdAt,
    )
}
