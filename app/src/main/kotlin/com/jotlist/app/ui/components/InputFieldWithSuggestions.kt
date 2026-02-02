package com.jotlist.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jotlist.app.domain.model.Suggestion
import com.jotlist.app.ui.theme.DarkGrey
import com.jotlist.app.ui.theme.ElectricBlue
import com.jotlist.app.ui.theme.InputSurface
import com.jotlist.app.ui.theme.Spacing
import com.jotlist.app.ui.theme.SurfaceCard

@Composable
fun InputFieldWithSuggestions(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    suggestions: List<Suggestion>,
    showSuggestions: Boolean,
    onSuggestionClick: (Suggestion) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(Spacing.md))
                .background(InputSurface)
                .padding(horizontal = Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Add item...",
                        color = DarkGrey
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = ElectricBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmit() }
                ),
                singleLine = true
            )

            IconButton(
                onClick = onSubmit,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add item",
                    tint = ElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showSuggestions && suggestions.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(top = 64.dp)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 144.dp)
                    .shadow(8.dp, RoundedCornerShape(Spacing.md))
                    .clip(RoundedCornerShape(Spacing.md))
                    .background(SurfaceCard)
            ) {
                items(
                    items = suggestions,
                    key = { it.id }
                ) { suggestion ->
                    Text(
                        text = suggestion.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSuggestionClick(suggestion) }
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                            .height(48.dp)
                    )

                    if (suggestion != suggestions.last()) {
                        HorizontalDivider(
                            color = DarkGrey,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}
