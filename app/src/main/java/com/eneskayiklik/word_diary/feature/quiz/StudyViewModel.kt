package com.eneskayiklik.word_diary.feature.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.swiper.SwipedOutDirection
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.tts.WordToSpeech
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListFilterType
import com.eneskayiklik.word_diary.util.extensions.calculateProficiencyLevel
import com.eneskayiklik.word_diary.util.extensions.filterIf
import com.eneskayiklik.word_diary.util.extensions.sumOf
import com.eneskayiklik.word_diary.util.extensions.sumOfAll
import com.eneskayiklik.word_diary.util.extensions.sumOfAllDouble
import com.eneskayiklik.word_diary.util.extensions.takeRandom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    private val tts: WordToSpeech,
    private val userPreference: UserPreferenceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _words = MutableStateFlow<List<WordEntity>>(emptyList())
    private var _filteredWords: List<WordEntity> = emptyList()

    private val _selectedFilters = MutableStateFlow(
        listOf(
            WordListFilterType.Beginner,
            WordListFilterType.Intermediate,
            WordListFilterType.Advanced,
            WordListFilterType.Expert
        )
    )

    private var _folderId: Int? = null
    private var _folderLang: String? = null

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _wordStatistics: HashMap<WordEntity, WordStatistics> = hashMapOf()

    private val _state = MutableStateFlow(StudyState())
    val state = combine(
        _state,
        _words,
        _selectedFilters
    ) { state, words, selectedFilters ->
        val ranges = selectedFilters.mapNotNull { it.proficiency }

        _filteredWords = words.filterIf(
            filter = { selectedFilters.contains(WordListFilterType.Favorite) },
            predicate = { it.isFavorite }
        ).filterIf(
            filter = { ranges.isNotEmpty() },
            predicate = { word -> ranges.any { word.proficiency.toInt() in it } }
        )

        state.copy(
            currentTotal = _filteredWords.size,
            selectedFilters = selectedFilters
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudyState())

    init {
        val folderId = savedStateHandle.get<Int>("folderId")
            ?: throw NullPointerException("folderId can not be null")
        val studyType = savedStateHandle.get<StudyType>("studyType")

        _state.update { it.copy(studyType = studyType) }

        getFolder(folderId)
        getWords(folderId)
    }

    fun onEvent(event: StudyEvent) {
        when (event) {
            is StudyEvent.OnWordStudyAction -> {
                val action = when (event.direction) {
                    SwipedOutDirection.RIGHT -> WordStudyAction.CorrectAnswer
                    SwipedOutDirection.LEFT -> WordStudyAction.WrongAnswer
                    else -> event.actionType
                }

                onWordStudyAction(
                    word = event.word,
                    action = action,
                    spentTime = event.totalSpentTime,
                    isFlashcard = event.direction != null
                )

                if (event.actionType == WordStudyAction.CorrectAnswer) {
                    onWordStudyQuestionAnswered(event.word)
                }
            }

            is StudyEvent.SpeakLoud -> tts.speak(event.word.meaning, _folderLang ?: return)
            is StudyEvent.ShowDialog -> showDialog(event.type)
            is StudyEvent.OnFilterSelected -> onFilterSelected(event.filter)
            StudyEvent.OnLooping -> _state.update { it.copy(isLoopingEnable = it.isLoopingEnable.not()) }
            StudyEvent.OnShuffle -> _state.update { it.copy(isShuffleEnable = it.isShuffleEnable.not()) }
            StudyEvent.OnTimer -> _state.update { it.copy(isTimerEnable = it.isTimerEnable.not()) }
            StudyEvent.OnSound -> _state.update { it.copy(isVoiceEnabled = it.isVoiceEnabled.not()) }
            StudyEvent.StartStudy -> startStudy()
            StudyEvent.FinishStudy -> finishStudy()
        }
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    private fun showDialog(type: StudyDialogType) = viewModelScope.launch {
        _state.update { it.copy(dialogType = type) }
    }

    private fun startStudy() = viewModelScope.launch {
        if (_filteredWords.isEmpty()) {
            onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            onEvent(UiEvent.ClearBackstack)
            return@launch
        }

        val studyState = _state.value
        val currentWords =
            if (studyState.isShuffleEnable) _filteredWords.shuffled() else _filteredWords

        val answers = if (
            studyState.studyType == StudyType.MultipleChoice
        ) (_filteredWords.takeRandom(
            3,
            listOf(currentWords.first())
        ) + currentWords.first()).shuffled() else emptyList()

        _state.update {
            it.copy(
                quizState = QuizState.Started,
                words = currentWords,
                choices = answers
            )
        }
        startTimer()
    }

    private fun finishStudy() = viewModelScope.launch {
        val studyType = _state.value.studyType ?: return@launch

        val totalHintCount = _wordStatistics.sumOf { it.hintTakeCount }.toDouble()
        val wrongAnswerCount = _wordStatistics.sumOf { it.wrongAnswerCount }.toDouble()
        val correctAnswerCount = _wordStatistics.sumOf { it.correctAnswerCount }.toDouble()
        val totalLetterCount = when (studyType) {
            StudyType.Write -> _wordStatistics.sumOfAll { wordEntity, wordStatistics ->
                wordEntity.word.length * wordStatistics.correctAnswerCount
            }

            else -> 0
        }

        val totalQuestionCount = _wordStatistics.sumOf { it.seenCount }

        val accuracy = when (studyType) {
            StudyType.FlashCard -> (correctAnswerCount / totalQuestionCount) * 100
            StudyType.Write -> (1.0 - (totalHintCount / totalLetterCount)) * 100
            StudyType.MultipleChoice -> maxOf(
                (1.0 - (wrongAnswerCount / (totalQuestionCount * 3))),
                .0
            ) * 100
        }

        if (totalQuestionCount == 0 && totalLetterCount == 0) {
            onEvent(UiEvent.ClearBackstack)
        } else {
            _state.update {
                it.copy(
                    quizState = QuizState.Finished,
                    studyResult = StudyResult(
                        accuracy = accuracy,
                        totalQuestionCount = totalQuestionCount,
                        timeSpent = _state.value.timerText,
                        proficiency = (_wordStatistics.sumOfAllDouble { wordEntity, wordStatistics ->
                            wordEntity.calculateProficiencyLevel(
                                wordStatistics,
                                studyType = studyType,
                                factor = 5.0
                            )
                        } + _filteredWords.sumOf { w -> w.proficiency }) / _filteredWords.size
                    )
                )
            }

            updateWordsStatistics(_wordStatistics, studyType)
            saveSessionResult(
                folderId = _folderId ?: return@launch,
                totalTimeSpend = _state.value.spendTime,
                accuracy = accuracy,
                studyType = studyType,
                wordStatistics = _wordStatistics
            )
            updateStreakCount()
        }
    }

    private fun updateStreakCount() = viewModelScope.launch(Dispatchers.IO) {
        userPreference.setStreakDay(System.currentTimeMillis())
    }

    private fun updateWordsStatistics(
        wordStatistics: HashMap<WordEntity, WordStatistics>,
        studyType: StudyType
    ) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.updateWordStatistics(wordStatistics, studyType)
    }

    private fun saveSessionResult(
        folderId: Int,
        totalTimeSpend: Long,
        accuracy: Double,
        studyType: StudyType,
        wordStatistics: HashMap<WordEntity, WordStatistics>
    ) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.addStudySession(
            StudySessionEntity(
                folderId = folderId,
                date = System.currentTimeMillis(),
                timeSpent = totalTimeSpend,
                accuracy = accuracy,
                sessionType = studyType,
                wordsInOrder = wordStatistics.map { it.key },
                statisticsInOrder = wordStatistics.map { it.value }
            )
        )
    }

    private fun onWordStudyQuestionAnswered(word: WordEntity) = viewModelScope.launch {
        val stateValue = _state.value
        if (_filteredWords.isEmpty()) {
            onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            onEvent(UiEvent.ClearBackstack)
            return@launch
        }
        val currentWords = stateValue.words.toMutableList()
        if (currentWords.size <= 2 && stateValue.isLoopingEnable) {
            if (stateValue.isShuffleEnable) {
                var shuffled = _filteredWords.shuffled()
                while (shuffled.first() == currentWords.last()) {
                    shuffled = _filteredWords.shuffled()
                }
                currentWords += shuffled
            } else {
                currentWords += _filteredWords
            }
        }
        val firstIndex = currentWords.indexOfFirst { it == word }
        if (firstIndex >= 0) currentWords.removeAt(firstIndex)

        if (currentWords.isEmpty()) {
            finishStudy()
        } else {
            when (stateValue.studyType) {
                StudyType.FlashCard -> onFlashcardStudyAction(currentWords)
                StudyType.Write -> onWriteStudyAction(currentWords)
                StudyType.MultipleChoice -> onMultipleChoiceStudyAction(currentWords)
                else -> return@launch
            }
        }
    }

    private fun onFlashcardStudyAction(currentWords: List<WordEntity>) = viewModelScope.launch {
        _state.update { it.copy(words = currentWords) }
    }

    private fun onWriteStudyAction(currentWords: List<WordEntity>) = viewModelScope.launch {
        delay(500)
        _state.update { it.copy(words = currentWords) }
    }

    private fun onMultipleChoiceStudyAction(currentWords: List<WordEntity>) =
        viewModelScope.launch {
            if (currentWords.isNotEmpty()) {
                val answers = (_filteredWords.takeRandom(
                    3,
                    listOf(currentWords.first())
                ) + currentWords.first()).shuffled()

                delay(500)
                _state.update { it.copy(words = currentWords, choices = answers) }
            } else {
                delay(500)
                _state.update { it.copy(words = currentWords, choices = emptyList()) }
            }
        }

    private fun onWordStudyAction(
        word: WordEntity,
        action: WordStudyAction,
        spentTime: Long,
        isFlashcard: Boolean
    ) = viewModelScope.launch {
        _wordStatistics[word] = _wordStatistics[word]?.let {
            WordStatistics(
                hintTakeCount = it.hintTakeCount + if (action == WordStudyAction.HintTaken) 1 else 0,
                wrongAnswerCount = it.wrongAnswerCount + if (action == WordStudyAction.WrongAnswer) 1 else 0,
                correctAnswerCount = it.correctAnswerCount + if (action == WordStudyAction.CorrectAnswer) 1 else 0,
                seenCount = it.seenCount + if (action == WordStudyAction.CorrectAnswer || isFlashcard) 1 else 0,
                totalSpentTime = it.totalSpentTime + spentTime
            )
        } ?: run {
            WordStatistics(
                hintTakeCount = if (action == WordStudyAction.HintTaken) 1 else 0,
                wrongAnswerCount = if (action == WordStudyAction.WrongAnswer) 1 else 0,
                correctAnswerCount = if (action == WordStudyAction.CorrectAnswer) 1 else 0,
                seenCount = if (action == WordStudyAction.CorrectAnswer || isFlashcard) 1 else 0,
                totalSpentTime = spentTime
            )
        }
    }

    private fun onFilterSelected(filterType: WordListFilterType) = viewModelScope.launch {
        val newList = _selectedFilters.value.toMutableList()
        if (newList.contains(filterType) && newList.size > 1) newList.remove(filterType)
        else if (newList.contains(filterType).not()) newList.add(filterType)
        _selectedFilters.update { newList }
    }

    private fun startTimer() = viewModelScope.launch(Dispatchers.IO) {
        val initialTime = System.currentTimeMillis()
        while (_state.value.quizState == QuizState.Started) {
            delay(1000)
            val currentTime = System.currentTimeMillis() - initialTime
            _state.update { it.copy(spendTime = currentTime) }
        }
    }

    private fun getFolder(
        folderId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        _folderId = folderId
        folderRepo.getFolder(folderId).collectLatest { folder ->
            _folderLang = folder.folderLangCode
            _state.update { it.copy(folder = folder) }
        }
    }

    private fun getWords(
        folderId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        _folderId = folderId
        folderRepo.getWords(folderId).collectLatest { words ->
            if (_words.value.isEmpty()) {
                _words.update { words }
                _state.update { it.copy(words = words) }
            }
        }
    }
}