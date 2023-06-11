package com.eneskayiklik.word_diary.feature.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserPreference
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.util.extensions.formatStatisticsTimer
import com.eneskayiklik.word_diary.util.extensions.getTimeShortName
import com.eneskayiklik.word_diary.util.extensions.toEpochDay
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    val userPrefs = userPreferenceRepository.userData.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreference()
    )

    private val _sevenDaysAgo = LocalDate.now().minusDays(6).toEpochDay()

    private var _sessions: List<StudySessionEntity>? = null
    private var _words: List<WordEntity>? = null

    init {
        getStudySessionData()
        getWords()
        collectUserPrefs()
    }

    private fun setupWordStatistics(words: List<WordEntity>) = viewModelScope.launch {
        _words = words

        val formatter = SimpleDateFormat("dd MM yyyy", Locale.ROOT)
        val today = formatter.format(System.currentTimeMillis())

        val totalSize = words.size
        val completeLearning = words.count { it.proficiency == 100.0 }
        val todayAddedWords = words.count { formatter.format(it.addedDate) == today }

        _state.update {
            it.copy(
                learningWordCount = totalSize - completeLearning,
                completeLearnedWordCount = completeLearning,
                todayNewWordCount = todayAddedWords
            )
        }

        _sessions?.let { sessions ->
            setupChart(words, sessions)
        }
    }

    private fun setupStudyStatistics(sessions: List<StudySessionEntity>) = viewModelScope.launch {
        _sessions = sessions

        val formatter = SimpleDateFormat("dd MM yyyy", Locale.ROOT)
        val today = formatter.format(System.currentTimeMillis())

        val todaySessions = sessions.filter { formatter.format(it.date) == today }
        val allTimeStudyTime = sessions.sumOf { it.timeSpent }
        val todayStudyTime = todaySessions.sumOf { it.timeSpent }

        val startDate =
            if (sessions.isEmpty()) System.currentTimeMillis() else sessions.first().date
        val startOfLearning = LocalDate.ofEpochDay(startDate.toEpochDay())
            .format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"))

        _state.update {
            it.copy(
                todayStudySessionCount = todaySessions.size,
                todayStudyTime = todayStudyTime.formatStatisticsTimer(),
                todayStudyTimeFormatter = todayStudyTime.getTimeShortName(),
                allTimeStudyTime = allTimeStudyTime.formatStatisticsTimer(),
                allTimeStudyTimeFormatter = allTimeStudyTime.getTimeShortName(),
                allTimeStudySessions = sessions.size,
                startOfLearning = startOfLearning
            )
        }

        _words?.let { words ->
            setupChart(words, sessions)
        }
    }

    /**
     * This function filters words and sessions by added date.
     * It only shows last seven day data including today.
     *
     */
    private fun setupChart(
        words: List<WordEntity>,
        sessions: List<StudySessionEntity>
    ) = viewModelScope.launch {
        // added word count
        // if (it.addedDate.toEpochDay() - _sevenDaysAgo - 6).absoluteValue == 0 this means it is today
        val wordLearnedLast7Day = words.filter { _sevenDaysAgo <= it.addedDate.toEpochDay() }
            .groupBy { (it.addedDate.toEpochDay() - _sevenDaysAgo - 6).absoluteValue }

        // studied unique words in last 7 days
        val sessionLast7Day = sessions.filter { _sevenDaysAgo <= it.date.toEpochDay() }
            .groupBy { (it.date.toEpochDay() - _sevenDaysAgo - 6).absoluteValue }

        val barEntry = mutableListOf<BarEntry>()

        repeat(7) { dayFromToday ->
            val learnedWordCount =
                wordLearnedLast7Day.getOrDefault(dayFromToday.toLong(), listOf()).size.toFloat()

            val studiedWordCount = sessionLast7Day.getOrDefault(dayFromToday.toLong(), listOf())
                .map { it.wordsInOrder.map { w -> w.wordId } }.flatten().toSet().size.toFloat()

            barEntry.add(
                // Bar data is reversed. If 'x' value is 0 it means it is 7 day ago
                // So if 'x' value is 6 it is today.
                BarEntry(
                    6F - dayFromToday, floatArrayOf(
                        learnedWordCount,
                        studiedWordCount
                    )
                )
            )
        }

        _state.update {
            it.copy(
                barEntry = barEntry
            )
        }
    }

    private fun getStudySessionData() = viewModelScope.launch(Dispatchers.IO) {
        folderRepository.getAllStudySessions().collectLatest {
            setupStudyStatistics(it)
        }
    }

    private fun getWords() = viewModelScope.launch(Dispatchers.IO) {
        folderRepository.getAllWords().collectLatest {
            setupWordStatistics(it)
        }
    }

    private fun collectUserPrefs() = viewModelScope.launch(Dispatchers.IO) {
        userPrefs.collectLatest { prefs ->
            _state.update {
                it.copy(
                    newWordDailyGoal = prefs.personalPrefs.newWordDailyGoal,
                    studySessionDailyGoal = prefs.personalPrefs.studySessionDailyGoal,
                    maxStreakCount = prefs.personalPrefs.maxStreakCount,
                    currentStreakCount = prefs.personalPrefs.currentStreakCount,
                    maxStreakFormatter = if (prefs.personalPrefs.maxStreakCount <= 1) R.string.day_singular else R.string.day_plural,
                    currentStreakFormatter = if (prefs.personalPrefs.currentStreakCount <= 1) R.string.day_singular else R.string.day_plural
                )
            }
        }
    }
}