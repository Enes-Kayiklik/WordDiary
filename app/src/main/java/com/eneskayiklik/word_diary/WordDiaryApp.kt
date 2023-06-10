package com.eneskayiklik.word_diary

import android.app.Application
import android.util.Log
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WordDiaryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initFlurry()
        initOneSignal()
    }

    private fun initFlurry() {
        FlurryAgent.Builder()
            .withCaptureUncaughtExceptions(BuildConfig.DEBUG.not())
            .withIncludeBackgroundSessionsInMetrics(BuildConfig.DEBUG.not())
            .withReportLocation(BuildConfig.DEBUG.not())
            .withLogLevel(Log.VERBOSE)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .build(this, BuildConfig.FLURRY_API_KEY)
    }

    private fun initOneSignal() {
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL_APP_ID)
    }
}