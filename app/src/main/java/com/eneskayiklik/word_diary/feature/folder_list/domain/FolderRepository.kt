package com.eneskayiklik.word_diary.feature.folder_list.domain

import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    fun getAllFolders(): Flow<List<FolderEntity>>

    fun getFoldersWithWordCount(userLangIso: String): Flow<List<FolderWithWordCount>>

    fun getFolderWithWords(folderId: Int): Flow<FolderWithWords>

    fun getFolder(folderId: Int): Flow<FolderEntity>

    fun addNewFolder(folder: FolderEntity)

    fun removeFolder(folderId: Int)

    fun updateFolderFavoriteState(folderId: Int, isFavorite: Boolean)

    fun updateWordFavoriteState(wordId: Int, folderId: Int, isFavorite: Boolean)

    fun getWord(folderId: Int, wordId: Int): Flow<WordEntity>

    fun getWords(folderId: Int): Flow<List<WordEntity>>

    fun getAllWords(): Flow<List<WordEntity>>

    fun addNewWord(word: WordEntity)

    fun removeWord(word: WordEntity)

    fun updateWordStatistics(
        wordStatistics: Map<WordEntity, WordStatistics>,
        studyType: StudyType
    )

    fun addStudySession(session: StudySessionEntity)

    fun getAllStudySessions(): Flow<List<StudySessionEntity>>
}