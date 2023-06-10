package com.eneskayiklik.word_diary.feature.quiz

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.eneskayiklik.word_diary.util.extensions.formatStudyTimer
import com.eneskayiklik.word_diary.util.extensions.getAccuracyStringRes
import com.eneskayiklik.word_diary.util.extensions.getHeadlineImage

data class StudyState(
    val folder: FolderEntity? = null,
    // Active words in study session
    val words: List<WordEntity> = emptyList(),
    // Multiple choice answers (only for multiple choice)
    val choices: List<WordEntity> = emptyList(),
    val spendTime: Long = 0L,
    val studyType: StudyType? = null,
    val quizState: QuizState = QuizState.Initial,
    val isTimerEnable: Boolean = true,
    val isShuffleEnable: Boolean = false,
    val isLoopingEnable: Boolean = true,
    val isVoiceEnabled: Boolean = true,
    val studyResult: StudyResult = StudyResult(),
    val dialogType: StudyDialogType = StudyDialogType.None
) {
    val isDialogActive = dialogType != StudyDialogType.None
    val timerText = spendTime.formatStudyTimer()
}

// [accuracy, proficiency] percentage between 0..100
data class StudyResult(
    val accuracy: Double = .0,
    val proficiency: Double = .0,
    val totalQuestionCount: Int = 0,
    val timeSpent: String = ""
) {
    val accuracyProgress: Float = (accuracy / 100).toFloat()
    val proficiencyProgress: Float = (proficiency / 100).toFloat()
    @DrawableRes
    val headlineImage: Int = accuracy.getHeadlineImage()
    @StringRes
    val kudosTextRes: Int = accuracy.getAccuracyStringRes()
}

data class WordStatistics(
    val hintTakeCount: Int = 0,
    val wrongAnswerCount: Int = 0,
    val correctAnswerCount: Int = 0,
    val seenCount: Int = 0,
    val totalSpentTime: Long = 0L
)

enum class StudyDialogType {
    None,
    Quit
}

enum class QuizState {
    Initial,
    Started,
    Finished
}

enum class WordStudyAction {
    CorrectAnswer,
    WrongAnswer,
    HintTaken
}