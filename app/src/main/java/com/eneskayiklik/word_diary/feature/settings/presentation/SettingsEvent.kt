package com.eneskayiklik.word_diary.feature.settings.presentation

import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.eneskayiklik.word_diary.core.data_store.data.AppTheme
import com.eneskayiklik.word_diary.core.data_store.data.ColorStyle
import com.eneskayiklik.word_diary.core.data_store.data.FontFamilyStyle
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage

sealed class SettingsEvent {

    data class UpdateNewWordGoal(val newValue: Int) : SettingsEvent()
    data class UpdateSessionGoal(val newValue: Int) : SettingsEvent()
    data class ShowDialog(val type: SettingsDialog) : SettingsEvent()
    data class SetAppLanguage(val lang: AppLanguage) : SettingsEvent()
    data class SetMotherLanguage(val lang: UserLanguage) : SettingsEvent()
    object UpdateMonochrome : SettingsEvent()
    object UpdateAmoledBlack : SettingsEvent()
    object OnWallpaperColor : SettingsEvent()
    data class PickTheme(val theme: AppTheme) : SettingsEvent()

    data class PickColorStyle(val style: ColorStyle) : SettingsEvent()

    data class PickFontFamily(val style: FontFamilyStyle) : SettingsEvent()

    data class PickColor(val color: Int) : SettingsEvent()
}
