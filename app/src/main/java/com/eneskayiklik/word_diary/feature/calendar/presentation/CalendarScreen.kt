package com.eneskayiklik.word_diary.feature.calendar.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Destination(style = ScreensAnim::class)
@Composable
fun CalendarScreen(
    navigator: DestinationsNavigator,
    //viewModel: ListsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

        }
    ) {
        Kalendar(
            currentDay = null,
            kalendarType = KalendarType.Firey,
            modifier = Modifier.padding(it),
            showLabel = true,
            events = KalendarEvents(),
            kalendarHeaderTextKonfig = null,
            kalendarColors = KalendarColors.default(),
            kalendarDayKonfig = KalendarDayKonfig.default(),
            daySelectionMode = DaySelectionMode.Single,
            dayContent = null,
            headerContent = null,
            onDayClick = { selectedDay, events ->
                // Handle day click event
            },
            onRangeSelected = { selectedRange, events ->
                // Handle range selection event
            },
            onErrorRangeSelected = { error ->
                // Handle error
            })
    }
}