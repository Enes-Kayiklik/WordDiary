package com.eneskayiklik.word_diary.di

import android.content.Context
import androidx.room.Room
import com.eneskayiklik.word_diary.core.database.WordDiaryDatabase
import com.eneskayiklik.word_diary.core.database.dao.WordDiaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideWordDiaryDb(
        @ApplicationContext context: Context
    ): WordDiaryDatabase = Room.databaseBuilder(
        context, WordDiaryDatabase::class.java, "word_diary.db"
    ).build()

    @Singleton
    @Provides
    fun provideWordDiaryDao(db: WordDiaryDatabase): WordDiaryDao = db.wordDiaryDao()
}