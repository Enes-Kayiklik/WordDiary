package com.eneskayiklik.word_diary.feature.create_folder.presentation

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.UserLanguage
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFolderViewModel @Inject constructor(
    private val folderRepo: FolderRepository,
    userPrefRepository: UserPreferenceRepository
) : ViewModel() {

    private val userPref = userPrefRepository.userData

    private val _state = MutableStateFlow(CreateFolderState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var _userLanguage: UserLanguage = UserLanguage.NOT_SPECIFIED

    init {
        setupUserPref()
    }

    private fun setupUserPref() = viewModelScope.launch {
        val initialData = userPref.first()
        _state.update {
            it.copy(
                languages = UserLanguage.values().filter { l -> l != initialData.userLanguage }
            )
        }
        _userLanguage = initialData.userLanguage
    }

    fun onEvent(event: CreateFolderEvent) {
        when (event) {
            CreateFolderEvent.CreateFolder -> createFolder()
            else -> Unit
        }
    }

    fun onFolderNameChanged(newValue: TextFieldValue) {
        if (newValue.text.length <= MAX_FOLDER_NAME_LEN) _state.update {
            it.copy(
                folderName = newValue,
                hasNameError = false
            )
        }
    }

    fun onLanguageSelected(item: UserLanguage) {
        _state.update { it.copy(selectedLanguage = item) }
    }

    fun onColorSelected(index: Int) {
        _state.update { it.copy(selectedColorIndex = index) }
    }

    fun onEmojiSelected(emoji: String) {
        _state.update { it.copy(selectedEmoji = emoji) }
    }

    private fun createFolder() = viewModelScope.launch(Dispatchers.IO) {
        val folderData = state.value

        if (folderData.folderName.text.isEmpty()) {
            _state.update { it.copy(hasNameError = true) }
            return@launch
        }

        if (_userLanguage == UserLanguage.NOT_SPECIFIED ||
            folderData.selectedLanguage == UserLanguage.NOT_SPECIFIED
        ) {
            _event.emit(UiEvent.ShowToast(textRes = R.string.warning_select_proper_language))
            return@launch
        }
        folderRepo.addNewFolder(
            FolderEntity(
                title = folderData.folderName.text,
                emoji = folderData.selectedEmoji,
                folderLangCode = folderData.selectedLanguage.isoCode,
                userLangCode = _userLanguage.isoCode,
                color = getDefaultColors()[state.value.selectedColorIndex].toArgb()
            )
        )
        _event.emit(UiEvent.ClearBackstack)
    }
}