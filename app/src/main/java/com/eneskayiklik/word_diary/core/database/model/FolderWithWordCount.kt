package com.eneskayiklik.word_diary.core.database.model

import androidx.room.Embedded
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity

data class FolderWithWordCount(
    @Embedded val folder: FolderEntity,
    val wordCount: Int
)
