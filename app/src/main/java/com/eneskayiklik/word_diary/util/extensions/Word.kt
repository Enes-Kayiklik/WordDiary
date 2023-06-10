package com.eneskayiklik.word_diary.util.extensions

import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType

fun WordEntity.calculateProficiencyLevel(
    statistics: WordStatistics,
    studyType: StudyType,
    factor: Double
): Double {
    return when (studyType) {
        StudyType.FlashCard -> statistics.correctAnswerCount.toDouble()
        StudyType.Write -> (1 - (statistics.hintTakeCount / (word.length.toDouble() * statistics.seenCount))) * statistics.seenCount
        StudyType.MultipleChoice -> maxOf(
            (1 - (statistics.wrongAnswerCount / (statistics.seenCount * 3.0))),
            .0
        ) * statistics.seenCount
    } * factor
}

fun WordEntity.calculateAccuracy(
    statistics: WordStatistics,
    studyType: StudyType
): Double {
    return when (studyType) {
        StudyType.FlashCard -> (statistics.correctAnswerCount / (statistics.correctAnswerCount + statistics.wrongAnswerCount).toDouble())
        StudyType.Write -> (1.0 - (statistics.hintTakeCount / (word.length * statistics.seenCount)))
        StudyType.MultipleChoice -> maxOf(
            (1.0 - (statistics.wrongAnswerCount / (statistics.seenCount * 3))),
            .0
        )
    } * 100
}