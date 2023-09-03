package com.eneskayiklik.word_diary.feature.calendar.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.core.util.getDefaultAnimationSpec
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    selectedDay: () -> CalendarDay,
    onDaySelected: (CalendarDay) -> Unit,
    dayNames: () -> List<String>
) {

    val today = remember {
        CalendarDay(
            date = LocalDate.now(),
            position = DayPosition.MonthDate
        )
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            dayNames().forEach { day ->
                Text(
                    text = day, modifier = Modifier.weight(1F),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                val textColor by animateColorAsState(
                    targetValue = when {
                        day == selectedDay() -> MaterialTheme.colorScheme.onPrimary
                        day == today -> MaterialTheme.colorScheme.primary
                        day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurface.copy(.3F)
                    }, label = "calendar_text_color",
                    animationSpec = getDefaultAnimationSpec()
                )

                val scaleAnim by animateFloatAsState(
                    targetValue = if (day == selectedDay()) 1F else 0F,
                    label = "calendar_background_color",
                    animationSpec = getDefaultAnimationSpec()
                )

                Box(modifier = Modifier
                    .align(Alignment.Center)
                    .size(36.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(
                        day.position == DayPosition.MonthDate,
                        onClick = {
                            onDaySelected(day)
                        }
                    ), contentAlignment = Alignment.Center)
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium)
                            .scale(scaleAnim)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "${day.date.dayOfMonth}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                    )

                    if (day == today && selectedDay() != today) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp)
                                .clip(CircleShape)
                                .size(4.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}