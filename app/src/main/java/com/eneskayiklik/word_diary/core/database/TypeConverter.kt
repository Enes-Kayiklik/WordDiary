package com.eneskayiklik.word_diary.core.database

import androidx.room.TypeConverter
import com.eneskayiklik.word_diary.core.database.entity.WordEntity
import com.eneskayiklik.word_diary.core.database.model.SampleSentence
import com.eneskayiklik.word_diary.core.database.model.SynonymSentence
import com.eneskayiklik.word_diary.feature.quiz.WordStatistics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverter {

    @TypeConverter
    fun stringToSynonymList(synonymSentences: String): List<SynonymSentence> {
        val listType: Type = object : TypeToken<List<SynonymSentence>>() {}.type
        return Gson().fromJson(synonymSentences, listType)
    }

    @TypeConverter
    fun synonymListToString(synonymSentences: List<SynonymSentence>): String {
        return Gson().toJson(synonymSentences)
    }

    @TypeConverter
    fun stringToSampleSentenceList(sampleSentences: String): List<SampleSentence> {
        val listType: Type = object : TypeToken<List<SampleSentence>>() {}.type
        return Gson().fromJson(sampleSentences, listType)
    }

    @TypeConverter
    fun sampleSentenceListToString(sampleSentences: List<SampleSentence>): String {
        return Gson().toJson(sampleSentences)
    }

    @TypeConverter
    fun stringToWordEntity(wordEntity: String): List<WordEntity> {
        val listType: Type = object : TypeToken<List<WordEntity>>() {}.type
        return Gson().fromJson(wordEntity, listType)
    }

    @TypeConverter
    fun wordEntityToString(wordEntity: List<WordEntity>): String {
        return Gson().toJson(wordEntity)
    }

    @TypeConverter
    fun stringToWordStatistics(wordEntity: String): List<WordStatistics> {
        val listType: Type = object : TypeToken<List<WordStatistics>>() {}.type
        return Gson().fromJson(wordEntity, listType)
    }

    @TypeConverter
    fun wordStatisticsToString(wordEntity: List<WordStatistics>): String {
        return Gson().toJson(wordEntity)
    }
}