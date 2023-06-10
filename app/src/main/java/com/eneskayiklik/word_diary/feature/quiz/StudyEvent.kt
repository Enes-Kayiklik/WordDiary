package com.eneskayiklik.word_diary.feature.quiz

import com.eneskayiklik.swiper.SwipedOutDirection
import com.eneskayiklik.word_diary.core.database.entity.WordEntity

sealed class StudyEvent {

    object OnTimer : StudyEvent()

    object OnShuffle : StudyEvent()

    object OnLooping : StudyEvent()

    object OnSound : StudyEvent()

    object StartStudy : StudyEvent()

    object FinishStudy : StudyEvent()

    data class ShowDialog(val type: StudyDialogType) : StudyEvent()

    data class SpeakLoud(val word: WordEntity) : StudyEvent()

    data class OnWordStudyAction(
        val word: WordEntity,
        val totalSpentTime: Long = 0L,
        val direction: SwipedOutDirection? = null,
        val actionType: WordStudyAction = WordStudyAction.CorrectAnswer
    ) : StudyEvent()
}