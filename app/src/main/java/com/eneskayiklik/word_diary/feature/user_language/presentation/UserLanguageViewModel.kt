package com.eneskayiklik.word_diary.feature.user_language.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLanguageViewModel @Inject constructor(
    private val preferenceRepository: UserPreferenceRepository
): ViewModel() {
    val userPrefs = preferenceRepository.userData

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEven(uiEvent: UiEvent) = viewModelScope.launch {
        _event.emit(uiEvent)
    }

    fun selectUserLanguage(language: UserLanguage) = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.setUserLanguage(language)
    }
}