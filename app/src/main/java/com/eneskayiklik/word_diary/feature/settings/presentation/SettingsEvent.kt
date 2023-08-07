package com.eneskayiklik.word_diary.feature.settings.presentation

import android.net.Uri
import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.eneskayiklik.word_diary.core.data_store.data.FontFamilyStyle
import com.eneskayiklik.word_diary.core.data_store.data.NotificationFrequency
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import java.time.LocalTime

sealed class SettingsEvent {

    data class UpdateNewWordGoal(val newValue: Int) : SettingsEvent()
    data class UpdateSessionGoal(val newValue: Int) : SettingsEvent()
    data class ShowDialog(val type: SettingsDialog) : SettingsEvent()
    data class SetAppLanguage(val lang: AppLanguage) : SettingsEvent()
    data class SetMotherLanguage(val lang: UserLanguage) : SettingsEvent()
    object UpdateMonochrome : SettingsEvent()
    object UpdateAmoledBlack : SettingsEvent()
    object OnWallpaperColor : SettingsEvent()
    data class UpdateNotificationEnabled(val enable: Boolean) : SettingsEvent()
    data class PickTheme(val theme: AppTheme) : SettingsEvent()
    data class PickColorStyle(val style: ColorStyle) : SettingsEvent()
    data class PickFontFamily(val style: FontFamilyStyle) : SettingsEvent()
    data class PickColor(val color: Int) : SettingsEvent()
    data class SelectTime(val time: LocalTime) : SettingsEvent()
    data class RestoreBackup(val uri: Uri?) : SettingsEvent()
    data class CreateBackup(val uri: Uri?) : SettingsEvent()
    data class PickNotificationFrequency(val frequency: NotificationFrequency) : SettingsEvent()
}
