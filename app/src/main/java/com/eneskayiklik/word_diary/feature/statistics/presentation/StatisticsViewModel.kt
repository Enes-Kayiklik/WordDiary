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
import java.util.Locale
import javax.inject.Inject

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

    init {
        getStudySessionData()
        getWords()
        collectUserPrefs()
    }

    private fun setupWordStatistics(words: List<WordEntity>) = viewModelScope.launch {
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
    }

    private fun setupStudyStatistics(sessions: List<StudySessionEntity>) = viewModelScope.launch {
        val formatter = SimpleDateFormat("dd MM yyyy", Locale.ROOT)
        val today = formatter.format(System.currentTimeMillis())

        val todaySessions = sessions.filter { formatter.format(it.date) == today }
        val allTimeStudyTime = sessions.sumOf { it.timeSpent }
        val todayStudyTime = todaySessions.sumOf { it.timeSpent }

        val startDate =
            if (sessions.isEmpty()) System.currentTimeMillis() else sessions.first().date
        val startOfLearning = SimpleDateFormat("dd MMMM, yyyy", Locale.ROOT).format(startDate)

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