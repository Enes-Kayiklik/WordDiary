package com.eneskayiklik.word_diary.feature.folder_list.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.destinations.CreateFolderScreenDestination
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    userPrefs: UserPreferenceRepository,
) : ViewModel() {
    private val _userPrefs = userPrefs.userData

    private val _state = MutableStateFlow(ListsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        collectUserLang()
    }

    @OptIn(ExperimentalAnimationApi::class)
    fun onEvent(event: FolderListEvent) {
        when (event) {
            FolderListEvent.AddFolder -> onEvent(UiEvent.OnNavigate(CreateFolderScreenDestination))
        }
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    private fun collectUserLang() = viewModelScope.launch(Dispatchers.IO) {
        _userPrefs.collectLatest {
            if (it.userLanguage != UserLanguage.NOT_SPECIFIED) getFolders(it.userLanguage.isoCode)
        }
    }

    private fun getFolders(
        userLangIso: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        folderRepo.getFoldersWithWordCount(userLangIso).collectLatest { new ->
            _state.update { it.copy(folders = new, isLoading = false) }
        }
    }
}