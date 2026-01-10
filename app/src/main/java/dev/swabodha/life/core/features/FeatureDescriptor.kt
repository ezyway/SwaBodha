package dev.swabodha.life.core.features

data class FeatureDescriptor(
    val id: String,
    val title: String,
    val iconRes: Int,
    val route: String,
    val enabled: Boolean = true
)
