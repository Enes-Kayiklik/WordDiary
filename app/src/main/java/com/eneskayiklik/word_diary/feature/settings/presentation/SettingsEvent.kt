package com.eneskayiklik.word_diary.feature.settings.presentation

import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage

sealed class SettingsEvent {

    data class UpdateNewWordGoal(val newValue: Int) : SettingsEvent()
    data class UpdateSessionGoal(val newValue: Int) : SettingsEvent()
    data class ShowDialog(val type: SettingsDialog) : SettingsEvent()
    data class SetAppLanguage(val lang: AppLanguage) : SettingsEvent()
    data class SetMotherLanguage(val lang: UserLanguage) : SettingsEvent()
}
