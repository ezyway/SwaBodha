package dev.swabodha.life.features.gym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.repository.GymRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GymViewModel(
    private val repo: GymRepository = GymRepository()
) : ViewModel() {

    val entries =
        repo.observeEntries()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun log(parts: List<BodyPart>) {
        viewModelScope.launch {
            repo.logWorkout(parts)
        }
    }
}
