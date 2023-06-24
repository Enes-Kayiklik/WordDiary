package com.eneskayiklik.word_diary.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eneskayiklik.word_diary.core.database.model.SampleSentence
import com.eneskayiklik.word_diary.core.database.model.SynonymSentence

@Entity(tableName = "word")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val wordId: Int = 0,
    val folderId: Int,
    val word: String,
    val meaning: String,
    val synonyms: List<SynonymSentence>,
    val samples: List<SampleSentence>,
    val isFavorite: Boolean,
    val addedDate: Long,
    val lastStudyDate: Long,
    val studyCount: Int,
    val accuracy: Double,
    val proficiency: Double,
    val totalSpendTime: Long,
)