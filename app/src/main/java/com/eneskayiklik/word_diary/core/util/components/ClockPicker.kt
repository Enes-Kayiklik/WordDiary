package com.eneskayiklik.word_diary.core.util.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ClockPicker(
    selectTime: (LocalTime) -> Unit,
    closePicker: () -> Unit
) {
    val selectedTime = remember { mutableStateOf(LocalTime.of(8, 20, 0)) }

    ClockDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { closePicker() }
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            selectTime(LocalTime.of(hours, minutes))
            selectedTime.value = LocalTime.of(hours, minutes, 0)
        },
        config = ClockConfig(
            defaultTime = selectedTime.value,
            is24HourFormat = true
        ),
    )
}