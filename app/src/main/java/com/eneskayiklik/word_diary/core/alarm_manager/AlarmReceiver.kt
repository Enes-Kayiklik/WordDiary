package com.eneskayiklik.word_diary.core.alarm_manager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.MainActivity
import com.eneskayiklik.word_diary.di.HiltBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ReminderManager

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            context = context,
            channelId = ALARM_NOTIFICATION_CHANNEL_ID
        )
        // Remove this line if you don't want to reschedule the reminder
        intent.getStringExtra(ReminderManager.ALARM_TIME_EXTRA)?.let { time ->
            reminderManager.enableReminder(time)
        }
    }

    companion object {
        const val ALARM_NOTIFICATION_CHANNEL_ID = "1999"
    }
}

fun NotificationManager.sendReminderNotification(
    context: Context,
    channelId: String,
) {
    val contentIntent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Content title")
        .setContentText("Content text")
        .setSmallIcon(R.drawable.ic_foreground)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

const val NOTIFICATION_ID = 1