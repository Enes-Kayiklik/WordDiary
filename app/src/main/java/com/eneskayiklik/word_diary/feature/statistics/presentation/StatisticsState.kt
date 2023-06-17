package com.eneskayiklik.word_diary.feature.statistics.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.eneskayiklik.word_diary.R
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.ads.nativead.NativeAd

@Stable
data class StatisticsState(
    val nativeAd: NativeAd? = null,
    val todayNewWordCount: Int = 0,
    val todayStudyTime: String = "",
    @StringRes val todayStudyTimeFormatter: Int = R.string.minute_format,
    val todayStudySessionCount: Int = 0,
    val learningWordCount: Int = 0,
    val completeLearnedWordCount: Int = 0,
    val allTimeStudyTime: String = "",
    @StringRes val allTimeStudyTimeFormatter: Int = R.string.minute_format,
    val allTimeStudySessions: Int = 0,
    val startOfLearning: String = "",
    val newWordDailyGoal: Int = 0,
    val studySessionDailyGoal: Int = 0,
    val maxStreakCount: Int = 1,
    val currentStreakCount: Int = 1,
    @StringRes val maxStreakFormatter: Int = R.string.day_singular,
    @StringRes val currentStreakFormatter: Int = R.string.day_singular,
    val barEntry: List<BarEntry> = emptyList()
) {
    val todayNewWords = "${minOf(todayNewWordCount, newWordDailyGoal)} / $newWordDailyGoal"
    val todayStudySessions =
        "${minOf(todayStudySessionCount, studySessionDailyGoal)} / $studySessionDailyGoal"

    val newWordProgress =
        if (newWordDailyGoal == 0) 0F else todayNewWordCount / newWordDailyGoal.toFloat()
}