package com.eneskayiklik.word_diary.core.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class ErrorOccurredSnackbar(
    override val message: String,
    override val actionLabel: String
): SnackbarVisuals {

    override val duration: SnackbarDuration
        get() = SnackbarDuration.Long

    override val withDismissAction: Boolean
        get() = true
}