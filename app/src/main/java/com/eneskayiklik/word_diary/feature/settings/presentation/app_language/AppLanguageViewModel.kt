package com.eneskayiklik.word_diary.feature.settings.presentation.app_language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.AppLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLanguageViewModel @Inject constructor(
    private val preferenceRepository: UserPreferenceRepository
): ViewModel() {
    val userPrefs = preferenceRepository.userData

    fun selectAppLanguage(language: AppLanguage) = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.setAppLanguage(language)
    }
}