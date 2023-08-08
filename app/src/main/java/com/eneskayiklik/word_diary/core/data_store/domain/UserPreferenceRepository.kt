package com.eneskayiklik.word_diary.core.data_store.domain

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.eneskayiklik.word_diary.core.data_store.data.FontFamilyStyle
import com.eneskayiklik.word_diary.core.data_store.data.NotificationFrequency
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface UserPreferenceRepository {

    val userData: Flow<UserPreference>

    suspend fun setAppLanguage(language: AppLanguage)

    suspend fun setUserLanguage(language: UserLanguage)

    suspend fun setAppTheme(theme: AppTheme)

    suspend fun setColor(color: Int)

    suspend fun setRandomColor()

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    suspend fun setColorFromWallpaper(context: Context)

    suspend fun setColorStyle(style: ColorStyle)

    suspend fun setFontFamily(style: FontFamilyStyle)

    suspend fun updateAmoledBlack()

    suspend fun updateColorfulBackground()

    suspend fun updateWallpaperColor()

    suspend fun updateRandomColor()

    suspend fun setSwipeAction(action: SwipeAction)

    suspend fun setNewWordDailyGoal(goal: Int)

    suspend fun setStudySessionDailyGoal(goal: Int)

    suspend fun setStreakDay(time: Long)

    suspend fun setShowOnboarding(show: Boolean)

    suspend fun enableAlarm(enable: Boolean)

    suspend fun setAlarm(time: LocalTime)

    suspend fun setNotificationFrequency(frequency: NotificationFrequency)
}