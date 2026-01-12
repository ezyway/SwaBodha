package dev.swabodha.life.features.weight.ui

import dev.swabodha.life.features.weight.data.entity.WeightEntryEntity

data class WeightUiState(
    val entries: List<WeightEntryEntity> = emptyList()
)
