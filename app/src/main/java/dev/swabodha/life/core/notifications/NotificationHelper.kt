package dev.swabodha.life.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.swabodha.life.R

object NotificationHelper {

    private const val CHANNEL_ID = "reminders"

    fun show(context: Context, title: String, message: String) {

        val sensitivePrefs =
            dev.swabodha.life.settings.data.SensitiveContentPrefs.get(context)

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        val hideSensitive = sensitivePrefs.isEnabled()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

        if (hideSensitive) {
            builder
                .setContentTitle("Reminder")
                .setContentText("Open app to view details")
                .setPublicVersion(
                    NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Reminder")
                        .setContentText("Content hidden")
                        .build()
                )
        } else {
            builder
                .setContentTitle(title)
                .setContentText(message)
        }

        manager.notify(title.hashCode(), builder.build())
    }
}
