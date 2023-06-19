package com.eneskayiklik.word_diary.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.paywall.domain.PaywallRepository
import com.eneskayiklik.word_diary.feature.paywall.presentation.PaywallDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val paywallRepository: PaywallRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
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
}