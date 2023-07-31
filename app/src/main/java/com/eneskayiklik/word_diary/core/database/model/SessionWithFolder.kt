package com.eneskayiklik.word_diary.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity

data class StudySessionWithFolder(
    @Embedded val session: StudySessionEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId"
    )
    val folder: FolderEntity
)
