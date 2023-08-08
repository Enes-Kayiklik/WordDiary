package com.eneskayiklik.word_diary.core.data_store.data

import androidx.annotation.StringRes
import androidx.compose.ui.text.font.FontFamily
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ui.theme.Caveat
import com.eneskayiklik.word_diary.core.ui.theme.DEFAULT_PRIMARY_COLOR
import com.eneskayiklik.word_diary.core.ui.theme.JosefinSans
import com.eneskayiklik.word_diary.core.ui.theme.Quicksand
import com.eneskayiklik.word_diary.core.ui.theme.Roboto
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class UserPreference(
    val userLanguage: UserLanguage = UserLanguage.values().firstOrNull {
        it.isoCode == Locale.getDefault().language
    } ?: UserLanguage.NOT_SPECIFIED,
    val appLanguage: AppLanguage = AppLanguage.FOLLOW_SYSTEM,
    val themePrefs: ThemePreference = ThemePreference(),
    val showOnboarding: Boolean = true,
    val notification: NotificationSettings = NotificationSettings(),
    val personalPrefs: PersonalPreference = PersonalPreference()
)

@Serializable
data class NotificationSettings(
    val isNotificationEnabled: Boolean = false,
    val notificationTime: String = "10:20",
    val notificationFrequency: NotificationFrequency = NotificationFrequency.OnceADay
)

@Serializable
data class ThemePreference(
    val appTheme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    val extractWallpaperColor: Boolean = false,
    val isAmoledBlack: Boolean = false,
    val randomColor: Boolean = false,
    val colorfulBackground: Boolean = false,
    val colorStyle: ColorStyle = ColorStyle.TonalSpot,
    val fontFamily: FontFamilyStyle = FontFamilyStyle.SANS_SERIF,
    val primaryColor: Int = DEFAULT_PRIMARY_COLOR
)

@Serializable
data class PersonalPreference(
    val swipeAction: SwipeAction = SwipeAction.SPEECH_LOUD,
    val newWordDailyGoal: Int = 5,
    val studySessionDailyGoal: Int = 3,
    val maxStreakCount: Int = 0,
    val currentStreakCount: Int = 0,
    val lastLoginDate: Long = 0L
)

enum class UserLanguage(
    val readable: String,
    val isoCode: String,
    val flagUnicode: String = ""
) {
    NOT_SPECIFIED(readable = "Not Specified", isoCode = ""),
    ENGLISH(readable = "English", isoCode = "en", flagUnicode = "\uD83C\uDDEC\uD83C\uDDE7"),
    CHINESE(readable = "中文", isoCode = "zh", flagUnicode = "\uD83C\uDDE8\uD83C\uDDF3"),
    GERMAN(readable = "Deutsch", isoCode = "de", flagUnicode = "\uD83C\uDDE9\uD83C\uDDEA"),
    ARABIC(readable = "العربية", isoCode = "ar", flagUnicode = "\uD83C\uDDF8\uD83C\uDDE6"),
    FRENCH(readable = "Français", isoCode = "fr", flagUnicode = "\uD83C\uDDF2\uD83C\uDDEB"),
    TURKISH(readable = "Türkçe", isoCode = "tr", flagUnicode = "\uD83C\uDDF9\uD83C\uDDF7"),
    HINDI(readable = "हिन्दी", isoCode = "hi", flagUnicode = "\uD83C\uDDEE\uD83C\uDDF3"),
    INDONESIA(readable = "Indonesia", isoCode = "id", flagUnicode = "\uD83C\uDDEE\uD83C\uDDE9"),
    SPANISH(readable = "Español", isoCode = "es", flagUnicode = "\uD83C\uDDEA\uD83C\uDDF8"),
    JAPANESE(readable = "日本語", isoCode = "ja", flagUnicode = "\uD83C\uDDEF\uD83C\uDDF5"),
    ITALIAN(readable = "Italiano", isoCode = "it", flagUnicode = "\uD83C\uDDEE\uD83C\uDDF9"),
    KOREAN(readable = "한국어", isoCode = "ko", flagUnicode = "\uD83C\uDDF0\uD83C\uDDF7"),
    POLISH(readable = "Polski", isoCode = "pl", flagUnicode = "\uD83C\uDDF5\uD83C\uDDF1"),
    PORTUGUESE(readable = "Português", isoCode = "pt", flagUnicode = "\uD83C\uDDF5\uD83C\uDDF9"),
    RUSSIAN(readable = "Русский", isoCode = "ru", flagUnicode = "\uD83C\uDDF7\uD83C\uDDFA")
}

enum class AppLanguage(
    @StringRes val stringRes: Int? = null,
    val readable: String = "",
    val isoCode: String = ""
) {
    FOLLOW_SYSTEM(stringRes = R.string.lang_follow_system),
    ENGLISH(readable = "English", isoCode = "en"),
    TURKISH(readable = "Türkçe", isoCode = "tr")
}

enum class AppTheme(@StringRes val title: Int) {
    DARK(R.string.darker_than_night),
    LIGHT(R.string.brighter_than_sun),
    FOLLOW_SYSTEM(R.string.follow_system)
}

enum class SwipeAction(@StringRes val title: Int, @StringRes val actionDesc: Int) {
    ADD_FAVORITES(
        R.string.add_remove_favorites,
        R.string.add_favorites
    ),
    SPEECH_LOUD(
        R.string.speech_loud,
        R.string.speech_loud_short
    ),
    EDIT_WORD(
        R.string.edit_word,
        R.string.edit_word_short
    ),
    DELETE_WORD(
        R.string.delete_from_list,
        R.string.delete_from_list_short
    ),
}

enum class ColorStyle(val title: String) {
    TonalSpot("Tonal spot"),
    Neutral("Neutral"),
    Vibrant("Vibrant"),
    Expressive("Expressive"),
    Rainbow("Rainbow"),
    FruitSalad("Fruit salad"),
    Monochrome(""),
    Fidelity("Fidelity"),
    Content("Content")
}

enum class NotificationFrequency(@StringRes val title: Int) {
    OnceADay(R.string.once_a_day),
    OneInThreeDays(R.string.one_in_three_day),
    OnceAWeek(R.string.once_a_week)
}

enum class FontFamilyStyle(val title: String, val family: FontFamily) {
    SANS_SERIF("Sans Serif", FontFamily.SansSerif),
    ROBOTO("Roboto", Roboto),
    MONOSPACE("Monospace", FontFamily.Monospace),
    QUICKSAND("Quicksand", Quicksand),
    CAVEAT("Caveat", Caveat),
    JOSEFIN_SANS("Josefin Sans", JosefinSans)
}