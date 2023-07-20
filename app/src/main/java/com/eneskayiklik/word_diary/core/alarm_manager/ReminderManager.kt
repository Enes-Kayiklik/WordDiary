package com.eneskayiklik.word_diary.core.alarm_manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.eneskayiklik.word_diary.util.extensions.updateBootReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun enableReminder(
        reminderTime: String,
        reminderId: Int = DEFAULT_REMINDER_ID
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val (hours, min) = reminderTime.split(":").map { it.toInt() }

        val intent = PendingIntent.getBroadcast(
            context,
            reminderId,
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(ALARM_TIME_EXTRA, reminderTime)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance(Locale.ROOT).apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
            set(Calendar.SECOND, 0)
        }

        // If reminderTime is in the past than add 1 day
        if (Calendar.getInstance(Locale.ROOT).apply {
                add(Calendar.MINUTE, 1)
            }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
            intent
        )
        context.updateBootReceiver(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
    }

    fun disableReminder(
        reminderId: Int = DEFAULT_REMINDER_ID
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = PendingIntent.getBroadcast(
            context,
            reminderId,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(intent)
        context.updateBootReceiver(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
    }

    companion object {
        const val DEFAULT_REMINDER_ID = 1881
        const val ALARM_TIME_EXTRA = "ALARM_TIME_EXTRA"
    }
}