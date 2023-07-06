package com.eneskayiklik.word_diary

import android.app.Application
import com.adapty.Adapty
import com.adapty.utils.AdaptyResult
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

    companion object {
        var hasPremium: Boolean = BuildConfig.DEBUG
    }
}