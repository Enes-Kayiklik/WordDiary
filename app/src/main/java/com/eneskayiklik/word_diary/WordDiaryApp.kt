package com.eneskayiklik.word_diary

import android.app.Application
import com.adapty.Adapty
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WordDiaryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        Adapty.activate(this, BuildConfig.ADAPTY_KEY)
        initOneSignal()
    }

    private fun initOneSignal() {
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL_APP_ID)
    }
}