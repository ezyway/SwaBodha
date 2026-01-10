package dev.swabodha.life.features.weight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.features.weight.data.repository.WeightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeightViewModel(
    private val repository: WeightRepository = WeightRepository()
) : ViewModel() {

    val uiState: StateFlow<WeightUiState> =
        repository.observeWeights()
            .map { WeightUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WeightUiState()
            )

    fun addWeight(weightKg: Float) {
        viewModelScope.launch {
            repository.addWeight(weightKg)
        }
    }
}
