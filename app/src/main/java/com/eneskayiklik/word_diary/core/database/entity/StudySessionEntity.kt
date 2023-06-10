package com.eneskayiklik.word_diary.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType

@Entity(tableName = "study_session")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Int = 0,
    val folderId: Int,
    val date: Long,
    val timeSpent: Long,
    val accuracy: Double,
    val wordsInOrder: List<WordEntity>, // wordsInOrder[0] == statisticsInOrder[0]
    val statisticsInOrder: List<WordStatistics>,
    val sessionType: StudyType
)
