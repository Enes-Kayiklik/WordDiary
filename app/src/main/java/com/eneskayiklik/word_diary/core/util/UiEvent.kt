package com.eneskayiklik.word_diary.core.util

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarVisuals
import com.eneskayiklik.word_diary.feature.folder_list.presentation.FolderListEvent
import com.ramcosta.composedestinations.spec.Direction
import java.util.UUID

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
    data class OnAdShown(val adId: UUID) : UiEvent()
}
