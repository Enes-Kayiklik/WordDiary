package com.eneskayiklik.word_diary.feature.statistics.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.ui.OnLifecycleEvent
import com.eneskayiklik.word_diary.core.ui.components.ad.MediumNativeAdView
import com.eneskayiklik.word_diary.core.util.ScreensAnim
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.PaywallScreenDestination
import com.eneskayiklik.word_diary.feature.statistics.presentation.component.CurrentStreakView
import com.eneskayiklik.word_diary.feature.statistics.presentation.component.GeneralStatisticsView
import com.eneskayiklik.word_diary.feature.statistics.presentation.component.StatisticsChart
import com.eneskayiklik.word_diary.util.TITLE_LETTER_SPACING
import com.eneskayiklik.word_diary.util.extensions.plus
import com.eneskayiklik.word_diary.util.extensions.shareStatistics
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Destination(style = ScreensAnim::class)
@Composable
fun StatisticsScreen(
    navigator: DestinationsNavigator,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is UiEvent.OnNavigate -> navigator.navigate(it.route)
                else -> Unit
            }
        }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> viewModel.onAdEvent(true)
            Lifecycle.Event.ON_PAUSE -> viewModel.onAdEvent(false)
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.statistics),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TITLE_LETTER_SPACING
                )
            }, actions = {
                IconButton(onClick = { context.shareStatistics(state) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = null
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(
                bottom = 96.dp,
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            ) + WindowInsets.navigationBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item("today_statistics") {
                Text(
                    text = stringResource(id = R.string.today),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                CurrentStreakView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(8.dp),
                    newWords = state.todayNewWords,
                    studyTime = state.todayStudyTime,
                    studyTimeFormatter = state.todayStudyTimeFormatter,
                    studySessions = state.todayStudySessions,
                    streakCount = state.currentStreakCount,
                    streakFormatter = state.currentStreakFormatter,
                    newWordProgress = state.newWordProgress
                )
            }

            item(key = "statistics_chart") {
                AnimatedVisibility(
                    visible = state.studiedBarEntry.isNotEmpty(),
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Progress", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        StatisticsChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                                .padding(8.dp),
                            studiedBarEntry = state.studiedBarEntry,
                            newWordBarEntry = state.newWordBarEntry
                        )
                    }
                }
            }

            item(key = "general_statistics") {
                Text(
                    text = stringResource(id = R.string.all_time),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                GeneralStatisticsView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp))
                        .padding(8.dp),
                    learningWord = state.learningWordCount,
                    completeLearned = state.completeLearnedWordCount,
                    studyTime = state.allTimeStudyTime,
                    studyTimeFormatter = state.allTimeStudyTimeFormatter,
                    studySessions = state.allTimeStudySessions,
                    startOfLearning = state.startOfLearning,
                    streakCount = state.maxStreakCount,
                    streakFormatter = state.maxStreakFormatter
                )
            }

            if (state.nativeAd != null && WordDiaryApp.hasPremium.not()) {
                item(key = "native_ad") {
                    MediumNativeAdView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(
                                MaterialTheme.colorScheme
                                    .surfaceColorAtElevation(12.dp)
                            ),
                        nativeAd = state.nativeAd,
                        onRemoveAds = {
                            navigator.navigate(PaywallScreenDestination)
                        }
                    )
                }
            }
        }
    }
}