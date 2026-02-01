package com.jotlist.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Checkboxes, Tags
    extraSmall = RoundedCornerShape(8.dp),
    // Buttons, Inputs
    small = RoundedCornerShape(16.dp),
    // Medium components
    medium = RoundedCornerShape(16.dp),
    // Cards, Dialogs
    large = RoundedCornerShape(24.dp),
    // Extra large components
    extraLarge = RoundedCornerShape(24.dp)
)

// Spacing tokens based on 8dp baseline grid
object Spacing {
    val xs = 4.dp   // Tight grouping
    val sm = 8.dp   // Related elements
    val md = 16.dp  // Component padding
    val lg = 24.dp  // Section separation
    val xl = 32.dp  // Major layout breaks
}

// Screen margins
val ScreenMargin = 20.dp
