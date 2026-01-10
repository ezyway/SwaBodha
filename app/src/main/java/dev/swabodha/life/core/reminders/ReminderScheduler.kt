package dev.swabodha.life.core.reminders

import android.content.Context
import androidx.work.*
import dev.swabodha.life.core.reminders.worker.ReminderWorker
import java.util.concurrent.TimeUnit
import java.util.Calendar

object ReminderScheduler {

    fun schedule(context: Context, reminder: Reminder) {
        val now = Calendar.getInstance()
        val trigger = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.hour)
            set(Calendar.MINUTE, reminder.minute)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = trigger.timeInMillis - now.timeInMillis

        val data = workDataOf(
            "title" to reminder.title,
            "message" to reminder.message
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        //Temp Testing - fires after 10 seconds
//        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
//            .setInitialDelay(10, TimeUnit.SECONDS)
//            .setInputData(data)
//            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                reminder.id,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    fun scheduleHourly(context: Context, reminder: Reminder) {
        val data = workDataOf(
            "title" to reminder.title,
            "message" to reminder.message
        )

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            1, TimeUnit.HOURS
        )
            .setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                reminder.id,
                ExistingPeriodicWorkPolicy.REPLACE,
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
