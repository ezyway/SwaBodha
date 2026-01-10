package dev.swabodha.life.features.water.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.core.reminders.repository.ReminderConfigRepository
import dev.swabodha.life.features.water.reminder.WaterReminder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterReminderViewModel(
    private val repo: ReminderConfigRepository = ReminderConfigRepository()
) : ViewModel() {

    val config =
        repo.observe(WaterReminder.ID)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun save(enabled: Boolean, interval: Int) {
        viewModelScope.launch {
            repo.save(
                id = WaterReminder.ID,
                enabled = enabled,
                intervalMinutes = interval
            )
        }
    }
}
