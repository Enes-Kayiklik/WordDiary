package com.eneskayiklik.word_diary.feature.calendar.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.feature.calendar.presentation.components.CalendarView
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.feature.calendar.presentation.components.SingleStudySessionView
import com.eneskayiklik.word_diary.feature.folder_list.presentation.component.EmptyDataView
import com.eneskayiklik.word_diary.util.extensions.plus
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Destination(style = ScreensAnim::class)
@Composable
fun CalendarScreen(
    navigator: DestinationsNavigator,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var screenTitle by remember { mutableStateOf("") }

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfGrid,
        firstDayOfWeek = DayOfWeek.SUNDAY
    )

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { calendarState.firstVisibleMonth }.collectLatest {
            screenTitle = "${
                it.yearMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } ${it.yearMonth.year}"
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = screenTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, actions = {

            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ), scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            CalendarView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                calendarState = calendarState,
                dayNames = { state.dayOfWeek },
                selectedDay = { state.selectedDay },
                onDaySelected = { day ->
                    viewModel.onScreenEvent(
                        CalendarScreenEvent.OnDaySelected(
                            day
                        )
                    )
                }
            )

            if (state.showEmptyView) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(bottom = 80.dp), contentAlignment = Alignment.Center
                ) {
                    EmptyDataView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = tween(durationMillis = 350)
                            ),
                        icon = Icons.Outlined.HistoryEdu,
                        title = state.selectedDayFormatted,
                        subtitle = stringResource(id = R.string.empty_study_session),
                        actionText = stringResource(id = R.string.practise),
                        showAction = state.isTodaySelected,
                        onAction = { navigator.navigateUp() }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 88.dp
                    ) + WindowInsets.navigationBars.asPaddingValues()
                ) {
                    val items = state.studySessions
                    items.forEachIndexed { index, item ->
                        item(key = item.session.sessionId) {
                            SingleStudySessionView(
                                studySession = item,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (index != items.lastIndex) item { Divider() }
                    }
                }
            }
        }
    }
}