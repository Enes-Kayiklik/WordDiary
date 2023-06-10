package com.eneskayiklik.word_diary.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity

data class FolderWithWords(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId"
    )
    val words: List<WordEntity>
)
