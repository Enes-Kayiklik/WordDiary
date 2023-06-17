package com.eneskayiklik.word_diary.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val folderId: Int = 0,
    @ColumnInfo(name = "folder_lang_code") val folderLangCode: String,
    @ColumnInfo(name = "user_lang_code") val userLangCode: String,
    val isFavorite: Boolean = false,
    val title: String,
    val color: Int,
    val emoji: String?
)