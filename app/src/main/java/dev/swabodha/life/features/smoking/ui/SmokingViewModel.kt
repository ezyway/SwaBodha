package dev.swabodha.life.features.smoking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.features.smoking.data.entity.SmokingEntryEntity
import dev.swabodha.life.features.smoking.data.entity.SmokingSize
import dev.swabodha.life.features.smoking.data.repository.SmokingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SmokingViewModel(
    private val repo: SmokingRepository = SmokingRepository()
) : ViewModel() {

    val entries =
        repo.observeEntries()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun log(
        count: Int,
        size: SmokingSize,
        isMenthol: Boolean
    ) {
        viewModelScope.launch {
            repo.log(count, size, isMenthol)
        }
    }

    fun remove(entry: SmokingEntryEntity) {
        viewModelScope.launch {
            repo.delete(entry)
        }
    }

    fun restore(entry: SmokingEntryEntity) {
        viewModelScope.launch {
            repo.insert(entry)
        }
    }
}
