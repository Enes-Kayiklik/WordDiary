package com.eneskayiklik.word_diary.feature.word_list.presentation

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.core.helper.ad.AdLoaderHelper
import com.eneskayiklik.word_diary.core.tts.WordToSpeech
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.create_word.presentation.DeletedSnackbar
import com.eneskayiklik.word_diary.feature.destinations.CreateWordScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.presentation.ListsViewModel
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
class WordListViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    private val tts: WordToSpeech,
    private val app: Application,
    private val adLoader: AdLoaderHelper,
    prefsRepo: UserPreferenceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _filterType = MutableStateFlow(
        listOf(
            WordListFilterType.Beginner,
            WordListFilterType.Intermediate,
            WordListFilterType.Advanced,
            WordListFilterType.Expert,
            WordListFilterType.Master
        )
    )
    private val _sortType = MutableStateFlow(WordListSortType.DateAdded)
    private val _sortDirection = MutableStateFlow(WordListSortDirection.Asc)
    private var _folderId: Int? = null
    private var _folderLang: String? = null
    private var _activeWord: WordEntity? = null

    private val _userPrefs = prefsRepo.userData

    private val _state = MutableStateFlow(WordListState())
    val state = combine(
        _state,
        _filterType,
        _sortType,
        _sortDirection,
        _userPrefs
    ) { state, filterType, sortType, sortDirection, userPrefs ->
        _folderLang = state.folder?.folder?.folderLangCode
        state.copy(
            filterType = filterType,
            sortType = sortType,
            sortDirection = sortDirection,
            folder = state.folder?.filterWords(filterType, sortType, sortDirection),
            swipeAction = userPrefs.personalPrefs.swipeAction
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WordListState())

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var _isAdActive: Boolean = false
    private var _isScreenVisible = false

    init {
        val folderId = savedStateHandle.get<Int>("folderId")
            ?: throw NullPointerException("folderId can not be null")
        getWordsInFolder(folderId)
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun onEvent(event: WordListEvent) {
        when (event) {
            WordListEvent.CreateWord -> _folderId?.let { folderId ->
                onEvent(
                    UiEvent.OnNavigate(
                        CreateWordScreenDestination(folderId = folderId, wordId = -1)
                    )
                )
            }

            WordListEvent.OnDeleteWord -> deleteWord(_activeWord ?: return)
            WordListEvent.OnDeleteList -> deleteList()
            WordListEvent.OnUndoDelete -> addWord(_activeWord ?: return)
            is WordListEvent.OnFilterSelected -> onFilterSelected(event.filterType)
            is WordListEvent.OnSortSelected -> _sortType.update { event.sortType }
            is WordListEvent.OnSortDirectionSelected -> _sortDirection.update { event.direction }
            is WordListEvent.OnWordAction -> onWordAction(event.word, event.action)
            is WordListEvent.OnWordClick -> onWordClick(event.word)
            is WordListEvent.OnAdEvent -> onAdEvent(event.startAd)
            is WordListEvent.OnSpeakEvent -> speakSentence(
                event.firstSentence,
                event.firstSource,
                event.secondSentence,
                event.secondSource
            )

            is WordListEvent.OnShowDialog -> _state.update {
                it.copy(
                    dialogType = event.type
                )
            }
        }
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    private fun updateFavoriteState(wordId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val currentState =
            _state.value.folder?.words?.firstOrNull { it.wordId == wordId }?.isFavorite
                ?: return@launch
        folderRepo.updateWordFavoriteState(wordId, _folderId ?: return@launch, currentState.not())
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun onWordAction(word: WordEntity, action: SwipeAction) {
        when (action) {
            SwipeAction.ADD_FAVORITES -> updateFavoriteState(word.wordId)
            SwipeAction.SPEECH_LOUD -> speakSentence(
                firstSentence = word.meaning,
                firstSource = _folderLang
            )

            //SwipeAction.MARK_AS_LEARNED -> Unit
            SwipeAction.EDIT_WORD -> onEvent(
                UiEvent.OnNavigate(
                    CreateWordScreenDestination(
                        word.folderId,
                        word.wordId
                    )
                )
            )

            SwipeAction.DELETE_WORD -> {
                _activeWord = word
                _state.update {
                    it.copy(
                        dialogType = WordListDialogType.DELETE
                    )
                }
            }
        }
    }

    private fun onWordClick(word: WordEntity?) = viewModelScope.launch {
        _state.update {
            it.copy(
                initialWordIndex = it.words.indexOf(word)
            )
        }
    }

    private fun speakSentence(
        firstSentence: String? = null,
        firstSource: String? = null,
        secondSentence: String? = null,
        secondSource: String? = null
    ) {
        try {
            if (firstSentence != null && firstSource != null) {
                tts.speak(firstSentence, firstSource)

            } else {
                tts.clearQueue()
            }
            if (secondSentence != null && secondSource != null) {
                tts.speakAddQueue(secondSentence, secondSource)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
        }
    }

    private fun deleteWord(word: WordEntity) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.removeWord(word)
        _state.update {
            it.copy(
                dialogType = WordListDialogType.NONE
            )
        }
        onEvent(
            UiEvent.ShowSnackbar(
                DeletedSnackbar(
                    message = app.getString(R.string.delete_word_snackbar_message),
                    actionLabel = app.getString(R.string.undo_action)
                )
            )
        )
    }

    private fun deleteList() = viewModelScope.launch(Dispatchers.IO) {
        _folderId?.let {
            folderRepo.removeFolder(it)
            onEvent(UiEvent.ClearBackstack)
        } ?: run {
            onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
        }
    }

    private fun addWord(word: WordEntity) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.addNewWord(word)
        onEvent(UiEvent.HideSnackbar)
    }

    private fun onFilterSelected(filterType: WordListFilterType) = viewModelScope.launch {
        val newList = _filterType.value.toMutableList()
        if (newList.contains(filterType) && newList.size > 1) newList.remove(filterType)
        else if (newList.contains(filterType).not()) newList.add(filterType)

        _filterType.update { newList }
    }

    private fun getWordsInFolder(
        folderId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        _folderId = folderId
        folderRepo.getFolderWithWords(folderId).collectLatest { new ->
            _state.update { it.copy(folder = new) }
            setupStatistics(new)
        }
    }

    private fun setupStatistics(folder: FolderWithWords) = viewModelScope.launch(Dispatchers.IO) {
        val totalWordCount = folder.words.size
        val finishedWordsCount = folder.words.count { it.proficiency >= 100.0 }
        val progress = folder.words.sumOf { it.proficiency } / totalWordCount
        _state.update {
            it.copy(
                listStatistic = ListStatistic(
                    progress = (progress / 100).toFloat(),
                    percentage = progress.toInt(),
                    totalWords = totalWordCount,
                    masteredWords = finishedWordsCount
                )
            )
        }
    }

    private fun onAdEvent(isStart: Boolean) = viewModelScope.launch {
        _isScreenVisible = isStart
        if (isStart) {
            if (_isAdActive.not()) startGetAd()
        }
    }

    private fun startGetAd() = viewModelScope.launch(Dispatchers.IO) {
        _isAdActive = true
        while (_isScreenVisible) {
            if (_state.value.words.isNotEmpty()) {
                val ad = adLoader.getNativeAd(1)
                _state.value.nativeAd?.destroy()
                _state.update { it.copy(nativeAd = ad) }
            }
            if (_state.value.nativeAd != null) {
                delay(ListsViewModel.AD_RELOAD_INTERVAL)
            } else {
                delay(5000)
            }
        }
        _isAdActive = false
    }
}