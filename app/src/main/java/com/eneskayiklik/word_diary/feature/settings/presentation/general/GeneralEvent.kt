package com.eneskayiklik.word_diary.feature.settings.presentation.general

import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import java.time.LocalTime

sealed class GeneralEvent {

    data class ShowDialog(val type: GeneralDialogType) : GeneralEvent()

    data class PickSwipeAction(val action: SwipeAction) : GeneralEvent()

    data class SetNewWordGoal(val value: Int) : GeneralEvent()

    data class SetStudySessionGoal(val value: Int) : GeneralEvent()

    data class SetAlarm(val time: LocalTime) : GeneralEvent()

    data class EnableAlarm(val enable: Boolean) : GeneralEvent()
}