package com.eneskayiklik.word_diary.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceRepository: UserPreferenceRepository
) : ViewModel() {
    val userPrefs = preferenceRepository.userData

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(uiEvent: UiEvent) = viewModelScope.launch {
        _event.emit(uiEvent)
    }

    fun selectUserLanguage(language: UserLanguage) = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.setUserLanguage(language)
    }

    fun finishOnboarding() = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.setShowOnboarding(false)
        onEvent(UiEvent.ClearBackstack)
    }

    fun setAlarm(time: LocalTime) = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.setAlarm(time)
    }

    fun enableAlarm(enable: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferenceRepository.enableAlarm(enable)
    }
}