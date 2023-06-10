package com.eneskayiklik.word_diary.core.data_store.data

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.ui.theme.DEFAULT_PRIMARY_COLOR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class UserPreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<UserPreference>
) : UserPreferenceRepository {

    override val userData: Flow<UserPreference>
        get() = dataStore.data

    override suspend fun setAppLanguage(language: AppLanguage) {
        try {
            dataStore.updateData {
                it.copy(
                    appLanguage = language
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setUserLanguage(language: UserLanguage) {
        try {
            dataStore.updateData {
                it.copy(
                    userLanguage = language
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        appTheme = theme
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setColor(color: Int) {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        primaryColor = color
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setRandomColor() {
        val color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        setColor(color)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override suspend fun setColorFromWallpaper(context: Context) {
        val colors =
            WallpaperManager.getInstance(context).getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
        setColor(colors?.primaryColor?.toArgb() ?: DEFAULT_PRIMARY_COLOR)
    }

    override suspend fun setColorStyle(style: ColorStyle) {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        colorStyle = style
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setFontFamily(style: FontFamilyStyle) {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        fontFamily = style
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateAmoledBlack() {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        isAmoledBlack = it.themePrefs.isAmoledBlack.not()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateWallpaperColor() {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        extractWallpaperColor = it.themePrefs.extractWallpaperColor.not()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateRandomColor() {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        randomColor = it.themePrefs.randomColor.not()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateColorfulBackground() {
        try {
            dataStore.updateData {
                it.copy(
                    themePrefs = it.themePrefs.copy(
                        colorfulBackground = it.themePrefs.colorfulBackground.not()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setSwipeAction(action: SwipeAction) {
        try {
            dataStore.updateData {
                it.copy(
                    personalPrefs = it.personalPrefs.copy(
                        swipeAction = action
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setNewWordDailyGoal(goal: Int) {
        try {
            dataStore.updateData {
                it.copy(
                    personalPrefs = it.personalPrefs.copy(
                        newWordDailyGoal = maxOf(0, minOf(goal, 99))
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setStudySessionDailyGoal(goal: Int) {
        try {
            dataStore.updateData {
                it.copy(
                    personalPrefs = it.personalPrefs.copy(
                        studySessionDailyGoal = maxOf(0, minOf(goal, 99))
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setShowOnboarding(show: Boolean) {
        try {
            dataStore.updateData {
                it.copy(
                    showOnboarding = show
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun enableAlarm(enable: Boolean) {
        try {
            dataStore.updateData {
                it.copy(
                    notification = it.notification.copy(
                        isNotificationEnabled = enable
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setAlarm(time: LocalTime) {
        try {
            dataStore.updateData {
                it.copy(
                    notification = it.notification.copy(
                        notificationTime = time.toString(),
                        isNotificationEnabled = true
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun setStreakDay(time: Long) {
        try {
            val currentData = dataStore.data.first().personalPrefs
            val dayFormatter = SimpleDateFormat("dd MM yyyy", Locale.ROOT)

            val today = dayFormatter.format(time)
            val yesterday = dayFormatter.format(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
            val lastLoginDate = dayFormatter.format(currentData.lastLoginDate)

            val (currentStreak, maxStreak) = when (lastLoginDate) {
                today -> return
                yesterday -> listOf(
                    currentData.currentStreakCount + 1,
                    maxOf(currentData.maxStreakCount, currentData.currentStreakCount + 1)
                )
                else -> listOf(1, maxOf(currentData.maxStreakCount, currentData.currentStreakCount))
            }

            dataStore.updateData {
                it.copy(
                    personalPrefs = it.personalPrefs.copy(
                        lastLoginDate = time,
                        maxStreakCount = maxStreak,
                        currentStreakCount = currentStreak
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}