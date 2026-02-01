package com.jotlist.app.domain.model

import com.jotlist.app.data.local.database.entity.ShoppingListEntity

/**
 * Domain model representing a shopping list.
 */
data class ShoppingList(
    val id: Long = 0,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
)

/**
 * Converts a ShoppingListEntity to a ShoppingList domain model.
 */
fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

/**
 * Converts a ShoppingList domain model to a ShoppingListEntity.
 */
fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
