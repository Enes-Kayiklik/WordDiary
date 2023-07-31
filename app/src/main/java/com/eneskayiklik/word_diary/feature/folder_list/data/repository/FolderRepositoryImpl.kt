package com.eneskayiklik.word_diary.feature.folder_list.data.repository

import com.eneskayiklik.word_diary.core.database.dao.WordDiaryDao
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.feature.folder_list.domain.FolderRepository
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.kizitonwose.calendar.core.CalendarDay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val dao: WordDiaryDao
) : FolderRepository {

    override fun getAllFolders(): Flow<List<FolderEntity>> = dao.getAllFolders()

    override fun getFoldersWithWordCount(userLangIso: String): Flow<List<FolderWithWordCount>> =
        dao.getFoldersWithWordCount(userLangIso)

    override fun getFolder(folderId: Int): Flow<FolderEntity> = dao.getFolder(folderId)

    override fun getFolderWithWords(folderId: Int): Flow<FolderWithWords> =
        dao.getFolderWithWords(folderId)

    override fun addNewFolder(folder: FolderEntity) = dao.addFolder(folder)

    override fun removeFolder(folderId: Int) = dao.removeFolderWithWords(folderId)

    override fun updateFolderFavoriteState(folderId: Int, isFavorite: Boolean) =
        dao.updateFolderFavoriteState(folderId, isFavorite)

    override fun updateWordFavoriteState(wordId: Int, folderId: Int, isFavorite: Boolean) =
        dao.updateWordFavoriteState(wordId, folderId, isFavorite)

    override fun getWord(folderId: Int, wordId: Int): Flow<WordEntity> =
        dao.getWord(folderId, wordId)

    override fun getWords(folderId: Int): Flow<List<WordEntity>> = dao.getWords(folderId)

    override fun getAllWords(): Flow<List<WordEntity>> = dao.getAllWords()

    override fun addNewWord(word: WordEntity) = dao.addWord(word)

    override fun removeWord(word: WordEntity) = dao.removeWord(word)

    override fun updateWordStatistics(
        wordStatistics: Map<WordEntity, WordStatistics>,
        studyType: StudyType
    ) = dao.updateWordStatistics(wordStatistics, studyType)

    override fun addStudySession(session: StudySessionEntity) = dao.addStudySession(session)

    override fun getAllStudySessions() = dao.getAllStudySession()
    override fun getStudySessionsAtDay(day: CalendarDay) = dao.getStudySessionsAtDay(
        lowerTime = day.date.toEpochDay() * 24L * 60L * 60L * 1000L,
        upperTime = (day.date.toEpochDay() + 1) * 24L * 60L * 60L * 1000L
    )
}