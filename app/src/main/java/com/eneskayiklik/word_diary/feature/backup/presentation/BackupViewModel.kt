package com.eneskayiklik.word_diary.feature.backup.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupRepository: BackupRepository,
    private val app: Application
) : ViewModel() {

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(BackupState())
    val state = _state.asStateFlow()

    fun createBackup(uri: Uri?) = viewModelScope.launch {
        if (uri == null) {
            _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            return@launch
        }

        backupRepository.backupToLocal(app, uri).collectLatest { result ->
            when (result) {
                Result.Loading -> _state.update { it.copy(isLocalBackupLoading = true) }
                is Result.Error -> _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
                is Result.Success -> {
                    _state.update { it.copy(isLocalBackupLoading = false) }
                    _event.emit(UiEvent.ShowToast(textRes = R.string.back_up_success))
                }
            }
        }
    }
}