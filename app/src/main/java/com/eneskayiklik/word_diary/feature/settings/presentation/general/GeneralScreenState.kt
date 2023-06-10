package com.eneskayiklik.word_diary.feature.settings.presentation.general

data class GeneralScreenState(
    val activeDialog: GeneralDialogType = GeneralDialogType.NONE
)

enum class GeneralDialogType {
    NONE,
    SWIPE_ACTION_PICKER
}