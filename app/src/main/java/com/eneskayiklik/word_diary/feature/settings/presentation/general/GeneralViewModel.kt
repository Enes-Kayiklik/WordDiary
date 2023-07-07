package com.eneskayiklik.word_diary.feature.settings.presentation.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GeneralScreenState())
    val state = _state.asStateFlow()

    val userPrefs = userPreferenceRepository.userData

    fun onEvent(event: GeneralEvent) = viewModelScope.launch {
        when (event) {
            is GeneralEvent.ShowDialog -> _state.update { it.copy(activeDialog = event.type) }
            is GeneralEvent.PickSwipeAction -> pickSwipeAction(event.action)
            is GeneralEvent.SetNewWordGoal -> setNewWordDailyGoal(event.value)
            is GeneralEvent.SetStudySessionGoal -> setStudySessionDailyGoal(event.value)
            is GeneralEvent.SetAlarm -> setAlarm(event.time)
            is GeneralEvent.EnableAlarm -> enableAlarm(event.enable)
        }
    }

    private fun setAlarm(time: LocalTime) = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.setAlarm(time)
    }

    private fun enableAlarm(enable: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.enableAlarm(enable)
    }

    private fun pickSwipeAction(action: SwipeAction) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(activeDialog = GeneralDialogType.NONE) }
        userPreferenceRepository.setSwipeAction(action)
    }

    private fun setNewWordDailyGoal(newValue: Int) = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.setNewWordDailyGoal(newValue)
    }

    private fun setStudySessionDailyGoal(newValue: Int) = viewModelScope.launch(Dispatchers.IO) {
        userPreferenceRepository.setStudySessionDailyGoal(newValue)
    }
}