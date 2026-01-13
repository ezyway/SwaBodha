package dev.swabodha.life.features.gym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import dev.swabodha.life.features.gym.data.repository.GymRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GymViewModel : ViewModel() {

    private val repo = GymRepository()
    private var lastDeleted: GymEntryEntity? = null

    val entries = repo.observeEntries()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun logAt(timeMillis: Long, parts: List<BodyPart>) {
        viewModelScope.launch {
            repo.logWorkoutAt(timeMillis, parts)
        }
    }

    fun delete(entry: GymEntryEntity) {
        lastDeleted = entry
        viewModelScope.launch {
            repo.delete(entry)
        }
    }

    fun undoDelete() {
        val entry = lastDeleted ?: return
        lastDeleted = null

        viewModelScope.launch {
            repo.insert(entry)
        }
    }
}
