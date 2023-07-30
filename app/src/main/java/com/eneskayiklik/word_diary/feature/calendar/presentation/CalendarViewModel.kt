package com.eneskayiklik.word_diary.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarScreenState())
    val state = _state.asStateFlow()

    init {
        getSessions(_state.value.selectedDay)
    }

    fun onScreenEvent(event: CalendarScreenEvent) {
        when (event) {
            is CalendarScreenEvent.OnDaySelected -> getSessions(day = event.day)
        }
    }

    private fun getSessions(
        day: CalendarDay
    ) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        _state.update {
            it.copy(
                selectedDay = day,
                studySessions = folderRepository.getStudySessionsAtDay(day)
            )
        }
    }
}