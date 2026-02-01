package com.jotlist.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jotlist.app.domain.model.ShoppingList
import com.jotlist.app.ui.theme.InputSurface
import com.jotlist.app.ui.theme.SoftGrey
import com.jotlist.app.ui.theme.Spacing
import com.jotlist.app.ui.theme.SurfaceCard

@Composable
fun ListCard(
    list: ShoppingList,
    itemCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .clip(RoundedCornerShape(Spacing.lg))
            .background(SurfaceCard)
            .clickable(onClick = onClick)
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(Spacing.sm))
                .background(InputSurface)
                .padding(Spacing.sm)
        ) {
            Icon(
                imageVector = Icons.Rounded.ShoppingCart,
                contentDescription = null,
                tint = SoftGrey,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = list.name,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = if (itemCount == 1) "1 item" else "$itemCount items",
            style = MaterialTheme.typography.bodySmall,
            color = SoftGrey
        )
    }
}
