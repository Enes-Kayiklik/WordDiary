package com.eneskayiklik.word_diary.feature.calendar.presentation

import com.eneskayiklik.word_diary.core.database.model.StudySessionWithFolder
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class CalendarScreenState(
    val dayOfWeek: List<String> = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY).map {
        it.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    },
    val selectedDay: CalendarDay = CalendarDay(
        LocalDate.now(),
        position = DayPosition.MonthDate
    ),
    val studySessions: List<StudySessionWithFolder> = emptyList()
) {
    val isTodaySelected = selectedDay.date == LocalDate.now()
    val showEmptyView = studySessions.isEmpty()
    val selectedDayFormatted: String = selectedDay.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}