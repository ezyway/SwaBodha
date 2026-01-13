package dev.swabodha.life.core.features

import androidx.compose.ui.graphics.vector.ImageVector

data class FeatureDescriptor(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String,
    val enabled: Boolean = true
)
