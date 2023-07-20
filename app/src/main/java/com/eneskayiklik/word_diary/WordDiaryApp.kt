package com.eneskayiklik.word_diary

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.adapty.Adapty
import com.adapty.utils.AdaptyResult
import com.eneskayiklik.word_diary.core.alarm_manager.AlarmReceiver
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WordDiaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationsChannels()
        MobileAds.initialize(this) {}
        Adapty.activate(this, BuildConfig.ADAPTY_KEY)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(BuildConfig.DEBUG.not())
        Firebase.analytics.setAnalyticsCollectionEnabled(BuildConfig.DEBUG.not())
        initOneSignal()
        updatePremiumStatus()
    }

    private fun updatePremiumStatus() {
        if (BuildConfig.DEBUG.not()) Adapty.getProfile { result ->
            when (result) {
                is AdaptyResult.Success -> {
                    hasPremium = result.value.accessLevels["premium"]?.isActive == true
                }

                is AdaptyResult.Error -> Unit
            }
        }
    }

    private fun initOneSignal() {
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL_APP_ID)
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlarmReceiver.ALARM_NOTIFICATION_CHANNEL_ID,
                getString(R.string.reminder_notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    companion object {
        var hasPremium: Boolean = BuildConfig.DEBUG
    }
}