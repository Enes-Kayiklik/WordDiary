package com.eneskayiklik.word_diary.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.database.model.FolderWithWordCount
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.eneskayiklik.word_diary.feature.word_list.domain.StudyType
import com.eneskayiklik.word_diary.util.extensions.calculateAccuracy
import com.eneskayiklik.word_diary.util.extensions.calculateProficiencyLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDiaryDao {

    @Query("SELECT * FROM folder")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Query(
        "SELECT folder.*, COUNT(word.wordId) AS wordCount FROM folder " +
                "LEFT JOIN word ON word.folderId = folder.folderId " +
                "WHERE folder.user_lang_code = :userLangIso " +
                "GROUP BY folder.folderId"
    )
    fun getFoldersWithWordCount(
        userLangIso: String,
    ): Flow<List<FolderWithWordCount>>

    @Query("SELECT * FROM folder WHERE folderId = :folderId")
    fun getFolder(folderId: Int): Flow<FolderEntity>

    @Transaction
    @Query("SELECT * FROM folder WHERE folderId = :folderId")
    fun getFolderWithWords(folderId: Int): Flow<FolderWithWords>

    @Query("UPDATE folder SET isFavorite = :isFavorite WHERE folderId = :folderId")
    fun updateFolderFavoriteState(folderId: Int, isFavorite: Boolean)

    @Query("UPDATE word SET isFavorite = :isFavorite WHERE wordId = :wordId AND folderId = :folderId")
    fun updateWordFavoriteState(wordId: Int, folderId: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFolder(data: FolderEntity)

    @Transaction
    fun removeFolderWithWords(folderId: Int) {
        removeFolder(folderId)
        removeWord(folderId)
    }

    @Query("DELETE FROM folder WHERE folderId = :folderId")
    fun removeFolder(folderId: Int)

    @Query("DELETE FROM word WHERE folderId = :folderId")
    fun removeWord(folderId: Int)

    @Query("SELECT * FROM word WHERE folderId = :folderId AND wordId = :wordId LIMIT 1")
    fun getWord(folderId: Int, wordId: Int): Flow<WordEntity>

    @Query("SELECT * FROM word WHERE folderId = :folderId")
    fun getWords(folderId: Int): Flow<List<WordEntity>>

    @Query("SELECT * FROM word")
    fun getAllWords(): Flow<List<WordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: WordEntity)

    @Delete
    fun removeWord(word: WordEntity)

    @Query(
        "UPDATE word SET accuracy = :accuracy, " +
                "proficiency = :proficiency, " +
                "totalSpendTime = totalSpendTime + :spentTime, " +
                "studyCount = studyCount + :seenCount " +
                "WHERE wordId = :id"
    )
    fun updateWordStatistics(
        id: Int,
        accuracy: Double,
        proficiency: Double,
        spentTime: Long,
        seenCount: Int
    )

    @Transaction
    fun updateWordStatistics(
        wordStatistics: Map<WordEntity, WordStatistics>,
        studyType: StudyType
    ) {
        for ((word, statistics) in wordStatistics) {
            val accuracy = word.calculateAccuracy(statistics, studyType)
            val proficiency = word.calculateProficiencyLevel(statistics, studyType, 5.0)

            updateWordStatistics(
                id = word.wordId,
                accuracy = accuracy,
                proficiency = minOf(word.proficiency + proficiency, 100.0),
                spentTime = statistics.totalSpentTime,
                seenCount = statistics.seenCount
            )
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStudySession(word: StudySessionEntity)

    @Query("SELECT * FROM study_session")
    fun getAllStudySession(): Flow<List<StudySessionEntity>>
}