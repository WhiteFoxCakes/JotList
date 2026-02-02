package com.jotlist.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jotlist.app.domain.model.ListItem
import com.jotlist.app.ui.theme.DarkGrey
import com.jotlist.app.ui.theme.NeonGreen
import com.jotlist.app.ui.theme.SoftGrey
import com.jotlist.app.ui.theme.Spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItemRow(
    item: ListItem,
    onCheckToggle: (ListItem) -> Unit,
    onEdit: (ListItem) -> Unit,
    onDelete: (ListItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = 4.dp)
            .combinedClickable(
                onClick = { onCheckToggle(item) },
                onLongClick = { onDelete(item) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onCheckToggle(item) },
            modifier = Modifier.size(48.dp)
        ) {
            if (item.isChecked) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Uncheck item",
                    tint = NeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(2.dp, DarkGrey),
                            shape = CircleShape
                        )
                )
            }
        }

        Text(
            text = item.text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (item.isChecked) SoftGrey else Color.White,
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Spacing.sm)
                .combinedClickable(
                    onClick = { onCheckToggle(item) },
                    onLongClick = { onDelete(item) },
                    onDoubleClick = { onEdit(item) }
                )
        )
    }
}
