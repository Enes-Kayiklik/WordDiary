package com.eneskayiklik.word_diary.core.alarm_manager

import android.content.Context
import android.content.Intent
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.di.HiltBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var userPreference: UserPreferenceRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule the notification
            CoroutineScope(Dispatchers.Main).launch {
                val notifPrefs = userPreference.userData.first().notification

                if (notifPrefs.isNotificationEnabled)
                    reminderManager.enableReminder(notifPrefs.notificationTime)
            }
        }
    }
}