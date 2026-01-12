package dev.swabodha.life.core.reminders

import android.content.Context
import androidx.work.*
import dev.swabodha.life.core.reminders.worker.ReminderWorker
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleAt(
        reminder: Reminder,
        triggerAtMillis: Long,
        context: Context,
    ) {
        val delay = triggerAtMillis - System.currentTimeMillis()
        if (delay <= 0) return

        val data = workDataOf(
            "title" to reminder.title,
            "message" to reminder.message
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                reminder.id,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }


    fun schedulePeriodic(
        context: Context,
        reminderId: String,
        title: String,
        message: String,
        intervalMinutes: Int
    ) {
        val data = workDataOf(
            "title" to title,
            "message" to message
        )

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            intervalMinutes.toLong(),
            TimeUnit.MINUTES
        ).setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                reminderId,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
    }


    fun cancel(context: Context, reminderId: String) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(reminderId)
    }


}
