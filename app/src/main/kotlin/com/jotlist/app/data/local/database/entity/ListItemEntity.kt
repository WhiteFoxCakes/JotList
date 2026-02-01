package com.jotlist.app.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing an item within a shopping list.
 *
 * @property originalPosition Stores the item's position before being checked,
 *           allowing it to return to the exact same position when unchecked.
 */
@Entity(
    tableName = "list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["list_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["list_id"])],
)
data class ListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "list_id")
    val listId: Long,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean = false,

    @ColumnInfo(name = "position")
    val position: Int,

    @ColumnInfo(name = "original_position")
    val originalPosition: Int? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
