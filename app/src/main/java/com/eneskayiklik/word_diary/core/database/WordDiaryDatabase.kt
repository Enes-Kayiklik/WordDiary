package com.eneskayiklik.word_diary.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eneskayiklik.word_diary.core.database.dao.WordDiaryDao
import com.eneskayiklik.word_diary.core.database.entity.FolderEntity
import com.eneskayiklik.word_diary.core.database.entity.StudySessionEntity
import com.eneskayiklik.word_diary.core.database.entity.WordEntity

@Database(
    entities = [FolderEntity::class, WordEntity::class, StudySessionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(TypeConverter::class)
abstract class WordDiaryDatabase : RoomDatabase() {

    abstract fun wordDiaryDao(): WordDiaryDao
}

/*object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'word' ADD COLUMN 'state' TEXT NOT NULL DEFAULT 'NewWord'")
        }
    }
}*/