package com.eneskayiklik.word_diary.feature.calendar.presentation

import com.kizitonwose.calendar.core.CalendarDay

sealed class CalendarScreenEvent {

    data class OnDaySelected(val day: CalendarDay) : CalendarScreenEvent()
}
