package dev.swabodha.life.core.reminders

import android.content.Context
import androidx.work.WorkManager

object ReminderStatus {

    fun isScheduled(
        context: Context,
        reminderId: String,
        onResult: (Boolean) -> Unit
    ) {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(reminderId)
            .observeForever { infos ->
                onResult(
                    infos.any {
                        !it.state.isFinished
                    }
                )
            }
    }
}
