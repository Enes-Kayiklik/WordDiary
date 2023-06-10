package com.eneskayiklik.word_diary.core.util

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarVisuals
import com.ramcosta.composedestinations.spec.Direction

sealed class UiEvent {
    data class OnNavigate(val route: Direction) : UiEvent()
    data class ScrollToIndex(val index: Int) : UiEvent()
    data class ShowSnackbar(val visuals: SnackbarVisuals) : UiEvent()

    data class SetAppLanguage(val iso: String) : UiEvent()
    data class ShowToast(val text: String? = null, @StringRes val textRes: Int? = null) : UiEvent()
    object HideSnackbar : UiEvent()
    object ClearBackstack : UiEvent()
    object ExpandSheet : UiEvent()
    object HideSheet : UiEvent()
}
