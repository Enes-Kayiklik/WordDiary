package com.eneskayiklik.word_diary.feature.settings.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.data_store.domain.UserPreferenceRepository
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.backup.domain.repository.BackupRepository
import com.eneskayiklik.word_diary.feature.backup.presentation.BackupDialog
import com.eneskayiklik.word_diary.feature.folder_list.presentation.UserData
import com.eneskayiklik.word_diary.feature.paywall.domain.PaywallRepository
import com.eneskayiklik.word_diary.feature.paywall.presentation.PaywallDialog
import com.eneskayiklik.word_diary.util.extensions.googleLoginClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val paywallRepository: PaywallRepository,
    private val preferenceRepo: UserPreferenceRepository,
    private val backupRepository: BackupRepository,
    private val app: Application
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        getLastGoogleUser()
        preferenceRepo.userData.onEach { prefs ->
            _state.update { it.copy(userPrefs = prefs) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    fun onEvent(event: SettingsEvent) = viewModelScope.launch {
        when (event) {
            is SettingsEvent.UpdateNewWordGoal -> preferenceRepo.setNewWordDailyGoal(event.newValue)
            is SettingsEvent.UpdateSessionGoal -> preferenceRepo.setStudySessionDailyGoal(event.newValue)

            is SettingsEvent.ShowDialog -> _state.update { it.copy(dialogType = event.type) }
            is SettingsEvent.SetAppLanguage -> preferenceRepo.setAppLanguage(event.lang)
            is SettingsEvent.SetMotherLanguage -> preferenceRepo.setUserLanguage(event.lang)
            is SettingsEvent.UpdateMonochrome -> preferenceRepo.updateColorfulBackground()
            is SettingsEvent.UpdateAmoledBlack -> preferenceRepo.updateAmoledBlack()
            is SettingsEvent.OnWallpaperColor -> preferenceRepo.updateWallpaperColor()
            is SettingsEvent.PickColor -> preferenceRepo.setColor(event.color)
            is SettingsEvent.PickColorStyle -> preferenceRepo.setColorStyle(event.style)
            is SettingsEvent.PickFontFamily -> preferenceRepo.setFontFamily(event.style)
            is SettingsEvent.PickTheme -> preferenceRepo.setAppTheme(event.theme)
            is SettingsEvent.UpdateNotificationEnabled -> preferenceRepo.enableAlarm(event.enable)
            is SettingsEvent.SelectTime -> preferenceRepo.setAlarm(event.time)
            is SettingsEvent.PickNotificationFrequency -> preferenceRepo.setNotificationFrequency(
                event.frequency
            )

            is SettingsEvent.RestoreBackup -> restoreBackup(event.uri)
            is SettingsEvent.CreateBackup -> createBackup(event.uri)
            is SettingsEvent.OnGoogleLogin -> onGoogleLogin(event.account)
            SettingsEvent.CreateDriveBackup -> createDriveBackup()
            SettingsEvent.OnGoogleLogout -> logOut()
        }
    }

    fun restorePurchase() = viewModelScope.launch(Dispatchers.IO) {
        val result = try {
            paywallRepository.restorePurchase()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

        when (result) {
            is Result.Success -> {
                WordDiaryApp.hasPremium = true
                onEvent(UiEvent.ShowToast(textRes = R.string.purchase_dialog_title))
            }

            is Result.Error -> onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            else -> Unit
        }
    }

    private fun createBackup(uri: Uri?) = viewModelScope.launch {
        if (uri == null) {
            _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            return@launch
        }

        backupRepository.backupToLocal(app, uri).collectLatest { result ->
            when (result) {
                Result.Loading -> Unit
                is Result.Error -> _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
                is Result.Success -> {
                    _event.emit(UiEvent.ShowToast(textRes = R.string.back_up_success))
                }
            }
        }
    }

    private fun restoreBackup(uri: Uri?) = viewModelScope.launch {
        if (uri == null) {
            _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            return@launch
        }

        backupRepository.restoreFromLocal(app, uri).collectLatest { result ->
            when (result) {
                Result.Loading -> Unit
                is Result.Error -> _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            dialogType = SettingsDialog.RestartApp
                        )
                    }
                }
            }
        }
    }

    private fun createDriveBackup() = viewModelScope.launch {
        if (_state.value.isDriveBackingUp) return@launch

        backupRepository.backupToDrive(app).collectLatest { result ->
            when (result) {
                Result.Loading -> _state.update { it.copy(isDriveBackingUp = true) }
                is Result.Error -> {
                    _state.update { it.copy(isDriveBackingUp = false) }
                    _event.emit(UiEvent.ShowToast(textRes = R.string.uncaught_error))
                }

                is Result.Success -> {
                    _state.update { it.copy(isDriveBackingUp = false) }
                    onEvent(UiEvent.ShowToast(textRes = R.string.back_up_success))
                }
            }
        }
    }

    private fun getLastGoogleUser() = viewModelScope.launch(Dispatchers.IO) {
        onGoogleLogin(GoogleSignIn.getLastSignedInAccount(app) ?: return@launch)
    }

    private fun onGoogleLogin(account: GoogleSignInAccount) = viewModelScope.launch {
        _state.update {
            it.copy(
                userData = it.userData.copy(
                    photoUrl = account.photoUrl,
                    displayName = account.displayName,
                    email = account.email
                )
            )
        }
    }

    private fun logOut() = viewModelScope.launch {
        app.googleLoginClient().signOut().addOnSuccessListener {
            _state.update {
                it.copy(userData = UserData())
            }
        }
    }
}